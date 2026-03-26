package dinhhung.mockmaster_server.service;

import dinhhung.mockmaster_server.dto.FolderDTO;
import dinhhung.mockmaster_server.entity.Folder;
import dinhhung.mockmaster_server.entity.Workspace;
import dinhhung.mockmaster_server.repository.FolderRepository;
import dinhhung.mockmaster_server.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final WorkspaceRepository workspaceRepository;

    @Transactional
    public FolderDTO createFolder(Long workspaceId, String folderName, String userId) {
        Workspace ws = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace không tồn tại"));

        // Kiểm tra quyền sở hữu
        if (!ws.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thực hiện thao tác này");
        }

        Folder folder = new Folder();
        folder.setFolderName(folderName);
        folder.setWorkspace(ws);

        Folder saved = folderRepository.save(folder);

        return FolderDTO.builder()
                .folderId(saved.getFolderId())
                .folderName(saved.getFolderName())
                .endpoints(new ArrayList<>())
                .build();
    }

    @Transactional
    public FolderDTO updateFolder(Long folderId, String newName, String userId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder không tồn tại"));

        if (!folder.getWorkspace().getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Không có quyền chỉnh sửa");
        }

        folder.setFolderName(newName);
        return FolderDTO.builder()
                .folderId(folder.getFolderId())
                .folderName(folder.getFolderName())
                .build();
    }

    @Transactional
    public void deleteFolder(Long folderId, String userId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new RuntimeException("Folder không tồn tại"));

        if (!folder.getWorkspace().getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Không có quyền xóa");
        }

        folderRepository.delete(folder);
    }
}