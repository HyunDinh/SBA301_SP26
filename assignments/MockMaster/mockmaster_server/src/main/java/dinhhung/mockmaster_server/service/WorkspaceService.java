package dinhhung.mockmaster_server.service;

import dinhhung.mockmaster_server.dto.FolderDTO;
import dinhhung.mockmaster_server.dto.MockEndpointDTO;
import dinhhung.mockmaster_server.dto.WorkspaceDTO;
import dinhhung.mockmaster_server.dto.WorkspaceFullDTO;
import dinhhung.mockmaster_server.entity.Folder;
import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.entity.Workspace;
import dinhhung.mockmaster_server.repository.FolderRepository;
import dinhhung.mockmaster_server.repository.UserAccountRepository;
import dinhhung.mockmaster_server.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserAccountRepository userRepository;
    private final FolderRepository folderRepository;

    // Helper: Chuyển đổi từ Entity sang DTO
    private WorkspaceDTO convertToDTO(Workspace ws) {
        return WorkspaceDTO.builder()
                .workspaceId(ws.getWorkspaceId())
                .workspaceName(ws.getWorkspaceName())
                .description(ws.getDescription())
                .folderCount(ws.getFolders() != null ? ws.getFolders().size() : 0)
                .ownerId(ws.getUser() != null ? ws.getUser().getUserId() : null)
                .build();
    }

    // Lấy danh sách Workspace của User
    @Transactional(readOnly = true)
    public List<WorkspaceDTO> getWorkspacesByUserId(String userId) {
        return workspaceRepository.findByUser_UserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 1. Tạo mới Workspace
    @Transactional
    public WorkspaceDTO createWorkspace(String userId, Workspace workspace) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Lấy giới hạn động từ SubscriptionPlan (FREE là 2, PREMIUM là 100)
        int limit = user.getSubscriptionPlan().getMaxWorkspaces();
        long currentCount = workspaceRepository.countByUser_UserId(userId);

        if (currentCount >= limit) {
            // Trả về một mã lỗi đặc biệt để Frontend nhận diện và mở trang thanh toán
            throw new RuntimeException("PAYMENT_REQUIRED: Bạn đã đạt giới hạn " + limit + " Workspace của gói " + user.getSubscriptionPlan().getPlanName());
        }

        workspace.setUser(user);
        return convertToDTO(workspaceRepository.save(workspace));
    }

    // 2. Cập nhật Workspace
    @Transactional
    public WorkspaceDTO updateWorkspace(String userId, Long workspaceId, Workspace details) {
        Workspace ws = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace không tìm thấy"));

        // Kiểm tra quyền sở hữu
        if (!ws.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa workspace này");
        }

        ws.setWorkspaceName(details.getWorkspaceName());
        ws.setDescription(details.getDescription());
        return convertToDTO(workspaceRepository.save(ws));
    }

    // 3. Xóa Workspace
    @Transactional
    public void deleteWorkspace(String userId, Long workspaceId) {
        Workspace ws = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace không tìm thấy"));

        if (!ws.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền xóa workspace này");
        }
        workspaceRepository.delete(ws);
    }

    // 4. Thêm Folder vào Workspace
    @Transactional
    public Folder addFolder(String userId, Long workspaceId, Folder folder) {
        Workspace ws = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace không tìm thấy"));

        if (!ws.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền truy cập workspace này");
        }

        folder.setWorkspace(ws);
        return folderRepository.save(folder);
    }

    // Trong WorkspaceService.java

    @Transactional(readOnly = true)
    public WorkspaceFullDTO getWorkspaceFullDetail(Long workspaceId, String userId) {
        Workspace ws = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace không tồn tại"));

        if (!ws.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền truy cập");
        }

        // Chuyển đổi toàn bộ cấu trúc sang DTO
        List<FolderDTO> folderDTOs = ws.getFolders().stream().map(folder ->
                FolderDTO.builder()
                        .folderId(folder.getFolderId())
                        .folderName(folder.getFolderName())
                        .endpoints(folder.getEndpoints().stream().map(ep ->
                                MockEndpointDTO.builder()
                                        .endpointId(ep.getEndpointId())
                                        .path(ep.getPath())
                                        .method(ep.getMethod())
                                        .statusCode(ep.getStatusCode())
                                        .responseBody(ep.getResponseBody())
                                        .contentType(ep.getContentType())
                                        .delayMs(ep.getDelayMs())
                                        .build()
                        ).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());

        return WorkspaceFullDTO.builder()
                .workspaceId(ws.getWorkspaceId())
                .workspaceName(ws.getWorkspaceName())
                .description(ws.getDescription())
                .folders(folderDTOs)
                .build();
    }

    // Trong WorkspaceService.java
    public FolderDTO convertFolderToDTO(Folder folder) {
        return FolderDTO.builder()
                .folderId(folder.getFolderId())
                .folderName(folder.getFolderName())
                // Nếu mới tạo folder chưa có endpoint thì trả về list rỗng
                .endpoints(List.of())
                .build();
    }
}