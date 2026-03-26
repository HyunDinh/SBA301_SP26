package dinhhung.mockmaster_server.service;

import dinhhung.mockmaster_server.dto.MockEndpointDTO;
import dinhhung.mockmaster_server.entity.Folder;
import dinhhung.mockmaster_server.entity.MockEndpoint;
import dinhhung.mockmaster_server.entity.SubscriptionPlan;
import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.repository.FolderRepository;
import dinhhung.mockmaster_server.repository.MockEndpointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MockEndpointService {

    private final MockEndpointRepository endpointRepository;
    private final FolderRepository folderRepository;

    @Transactional
    public MockEndpointDTO createEndpoint(Long folderId, MockEndpointDTO dto, String userId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder không tồn tại"));

        UserAccount user = folder.getWorkspace().getUser();
        SubscriptionPlan plan = user.getSubscriptionPlan();

        // 1. Kiểm tra quyền sở hữu workspace
        if (!user.getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thao tác trên Workspace này");
        }

        // 2. Chuẩn hóa Path & Method
        String path = (dto.getPath() != null && !dto.getPath().isEmpty()) ? dto.getPath() : "/new-api";
        String method = (dto.getMethod() != null && !dto.getMethod().isEmpty()) ? dto.getMethod() : "GET";

        // 3. Kiểm tra trùng lặp Path & Method trong cùng 1 Folder
        if (endpointRepository.existsByPathAndMethodAndFolder_FolderId(path, method, folderId)) {
            throw new RuntimeException("Lỗi: API [" + method + "] " + path + " đã tồn tại trong folder này!");
        }

        // 4. Kiểm tra giới hạn API theo Gói cước (Tận dụng SubscriptionPlan)
        int maxApis = plan.getMaxEndpointsPerWorkspace();
        long currentApiCount = endpointRepository.countByFolder_Workspace_WorkspaceId(folder.getWorkspace().getWorkspaceId());

        if (currentApiCount >= maxApis) {
            throw new RuntimeException("Workspace đã đạt giới hạn tối đa " + maxApis +
                    " API của gói " + plan.getPlanName() + ". Vui lòng nâng cấp!");
        }

        // 5. Kiểm tra tính năng Delay (Chỉ dành cho PREMIUM)
        boolean isPremium = "PREMIUM".equalsIgnoreCase(plan.getPlanName());
        if (!isPremium && dto.getDelayMs() != null && dto.getDelayMs() > 0) {
            throw new RuntimeException("Tính năng 'Response Delay' chỉ dành cho tài khoản PREMIUM.");
        }

        // 6. Lưu Entity
        MockEndpoint ep = new MockEndpoint();
        ep.setPath(path);
        ep.setMethod(method);
        ep.setStatusCode(dto.getStatusCode() != null ? dto.getStatusCode() : 200);
        ep.setContentType(dto.getContentType() != null ? dto.getContentType() : "application/json");
        ep.setResponseBody(dto.getResponseBody() != null ? dto.getResponseBody() : "{}");

        // Cưỡng ép delay = 0 nếu không phải Premium
        ep.setDelayMs(isPremium && dto.getDelayMs() != null ? dto.getDelayMs() : 0);

        ep.setFolder(folder);

        return convertToDTO(endpointRepository.save(ep));
    }

    @Transactional
    public MockEndpointDTO updateEndpoint(Long id, MockEndpointDTO dto, String userId) {
        MockEndpoint ep = endpointRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endpoint không tồn tại"));

        UserAccount user = ep.getFolder().getWorkspace().getUser();
        SubscriptionPlan plan = user.getSubscriptionPlan();

        if (!user.getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa API này");
        }

        // Kiểm tra trùng lặp khi thay đổi Path hoặc Method
        if (!ep.getPath().equals(dto.getPath()) || !ep.getMethod().equals(dto.getMethod())) {
            if (endpointRepository.existsByPathAndMethodAndFolder_FolderId(dto.getPath(), dto.getMethod(), ep.getFolder().getFolderId())) {
                throw new RuntimeException("Không thể cập nhật: Cặp Path và Method này đã tồn tại!");
            }
        }

        // Kiểm tra tính năng Delay khi update
        boolean isPremium = "PREMIUM".equalsIgnoreCase(plan.getPlanName());
        if (!isPremium && dto.getDelayMs() != null && dto.getDelayMs() > 0) {
            throw new RuntimeException("Vui lòng nâng cấp PREMIUM để tùy chỉnh thời gian Delay.");
        }

        ep.setPath(dto.getPath());
        ep.setMethod(dto.getMethod());
        ep.setStatusCode(dto.getStatusCode());
        ep.setResponseBody(dto.getResponseBody());
        ep.setContentType(dto.getContentType());
        ep.setDelayMs(isPremium ? dto.getDelayMs() : 0);

        return convertToDTO(endpointRepository.save(ep));
    }

    @Transactional
    public void deleteEndpoint(Long id, String userId) {
        MockEndpoint ep = endpointRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endpoint không tồn tại"));

        if (!ep.getFolder().getWorkspace().getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền xóa API này");
        }
        endpointRepository.delete(ep);
    }

    private MockEndpointDTO convertToDTO(MockEndpoint ep) {
        return MockEndpointDTO.builder()
                .endpointId(ep.getEndpointId())
                .path(ep.getPath())
                .method(ep.getMethod())
                .statusCode(ep.getStatusCode())
                .contentType(ep.getContentType())
                .responseBody(ep.getResponseBody())
                .delayMs(ep.getDelayMs())
                .build();
    }
}