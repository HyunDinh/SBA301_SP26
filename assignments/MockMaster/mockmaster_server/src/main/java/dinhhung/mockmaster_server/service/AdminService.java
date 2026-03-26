package dinhhung.mockmaster_server.service;

import dinhhung.mockmaster_server.dto.AdminDashboardStats;
import dinhhung.mockmaster_server.dto.UserSummaryDTO;
import dinhhung.mockmaster_server.dto.UserWorkspaceDetailDTO;
import dinhhung.mockmaster_server.entity.SubscriptionPlan;
import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserAccountRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MockEndpointRepository endpointRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final SubscriptionPlanRepository planRepository;

    @Transactional(readOnly = true)
    public AdminDashboardStats getStats() {
        log.info("=== [AdminService] START FETCHING DETAILED DASHBOARD STATS ===");

        try {
            long userCount = userRepository.count();
            long wsCount = workspaceRepository.count();
            long epCount = endpointRepository.count();

            Double revenue = transactionRepository.findAll().stream()
                    .filter(t -> "SUCCESS".equalsIgnoreCase(t.getStatus()))
                    .mapToDouble(t -> t.getAmount() != null ? t.getAmount() : 0.0)
                    .sum();

            List<UserSummaryDTO> recentUsers = userRepository.findAll().stream()
                    .limit(10)
                    .map(u -> {
                        String plan = (u.getSubscriptionPlan() != null) ? u.getSubscriptionPlan().getPlanName() : "N/A";

                        // Ánh xạ danh sách Workspace chi tiết
                        List<UserWorkspaceDetailDTO> workspaceDetails = (u.getWorkspaces() == null) ? List.of() :
                                u.getWorkspaces().stream()
                                        .map(ws -> {
                                            // TÍNH TOÁN SỐ LƯỢNG API:
                                            // Duyệt qua tất cả các Folders trong Workspace, cộng tổng số Endpoints của từng Folder
                                            int totalApis = 0;
                                            if (ws.getFolders() != null) {
                                                totalApis = ws.getFolders().stream()
                                                        .filter(f -> f.getEndpoints() != null)
                                                        .mapToInt(f -> f.getEndpoints().size())
                                                        .sum();
                                            }

                                            return UserWorkspaceDetailDTO.builder()
                                                    .workspaceName(ws.getWorkspaceName()) // Theo đúng Entity của Hùng
                                                    .apiCount(totalApis)
                                                    .build();
                                        })
                                        .collect(Collectors.toList());

                        return UserSummaryDTO.builder()
                                .userId(u.getUserId())
                                .email(u.getEmail())
                                .planName(plan)
                                .workspaceCount(workspaceDetails.size())
                                .status(u.getStatus())
                                .workspaces(workspaceDetails)
                                .build();
                    })
                    .collect(Collectors.toList());

            return AdminDashboardStats.builder()
                    .totalUsers(userCount)
                    .totalWorkspaces(wsCount)
                    .totalEndpoints(epCount)
                    .totalRevenue(revenue)
                    .recentUsers(recentUsers)
                    .build();

        } catch (Exception e) {
            log.error("!!! [AdminService] ERROR: ", e);
            throw e;
        }
    }

    @Transactional
    public void updateUserPlan(String userId, String planName) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        SubscriptionPlan plan = planRepository.findByPlanName(planName)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planName));

        user.setSubscriptionPlan(plan);
        userRepository.save(user);
    }

    @Transactional
    public void updateUserStatus(String userId, String status) {
        log.info("[AdminService] Updating status for user: {} to {}", userId, status);

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // Chuẩn hóa status (ví dụ: ACTIVE, LOCKED)
        user.setStatus(status.toUpperCase());
        userRepository.save(user);

        log.info("-> Status updated successfully for user: {}", userId);
    }
}