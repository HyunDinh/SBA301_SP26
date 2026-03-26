package dinhhung.mockmaster_server.service;

import dinhhung.mockmaster_server.entity.MockEndpoint;
import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.repository.MockEndpointRepository;
import dinhhung.mockmaster_server.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MockEngineService {

    private final MockEndpointRepository endpointRepository;
    private final UserAccountRepository userRepository;

    public Optional<MockEndpoint> getMockResponse(String domain, Long workspaceId, String path, String method) {
        Optional<UserAccount> userOpt = userRepository.findBySystemDomain(domain);
        if (userOpt.isEmpty() || "LOCKED".equalsIgnoreCase(userOpt.get().getStatus())) {
            return Optional.empty();
        }

        UserAccount user = userOpt.get();
        Optional<MockEndpoint> endpointOpt = endpointRepository.findBySystemDomainAndWorkspaceAndPath(domain, workspaceId, path, method);

        endpointOpt.ifPresent(ep -> {
            // Chỉ thực thi delay nếu là user PREMIUM
            if ("PREMIUM".equalsIgnoreCase(user.getSubscriptionPlan().getPlanName())) {
                if (ep.getDelayMs() != null && ep.getDelayMs() > 0) {
                    try {
                        Thread.sleep(ep.getDelayMs());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        return endpointOpt;
    }

    // Hàm bổ trợ để Controller phân biệt giữa "Bị khóa" và "Không tìm thấy"
    public boolean isAccountLocked(String domain) {
        return userRepository.findBySystemDomain(domain)
                .map(u -> "LOCKED".equalsIgnoreCase(u.getStatus()))
                .orElse(false);
    }
}