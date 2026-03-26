package dinhhung.mockmaster_server.controller;

import dinhhung.mockmaster_server.dto.FolderDTO;
import dinhhung.mockmaster_server.dto.WorkspaceDTO;
import dinhhung.mockmaster_server.entity.Folder;
import dinhhung.mockmaster_server.entity.Workspace;
import dinhhung.mockmaster_server.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
@Slf4j
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    // Lấy danh sách workspace của user đang đăng nhập
    @GetMapping
    public ResponseEntity<List<WorkspaceDTO>> getAll(Authentication auth) {
        // auth.getName() lấy userId từ JwtFilter
        return ResponseEntity.ok(workspaceService.getWorkspacesByUserId(auth.getName()));
    }

    // Lấy danh sách cho một userId cụ thể (nếu cần thiết cho Admin hoặc debug)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkspaceDTO>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(workspaceService.getWorkspacesByUserId(userId));
    }

    // Tạo mới workspace
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Workspace workspace, Authentication auth) {
        try {
            log.info("[WorkspaceController] Tạo workspace: {}", workspace.getWorkspaceName());
            return ResponseEntity.ok(workspaceService.createWorkspace(auth.getName(), workspace));
        } catch (RuntimeException e) {
            // Trả về lỗi 400 kèm message để Frontend nhận diện LIMIT_REACHED
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Cập nhật workspace
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Workspace workspace, Authentication auth) {
        try {
            WorkspaceDTO updated = workspaceService.updateWorkspace(auth.getName(), id, workspace);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa workspace
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        try {
            workspaceService.deleteWorkspace(auth.getName(), id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Thêm Folder vào Workspace
    @PostMapping("/{id}/folders")
    public ResponseEntity<?> addFolder(@PathVariable Long id, @RequestBody Folder folder, Authentication auth) {
        try {
            // Lưu Folder và nhận về Entity
            Folder savedFolder = workspaceService.addFolder(auth.getName(), id, folder);

            // Map sang DTO trước khi trả về cho Client
            FolderDTO response = workspaceService.convertFolderToDTO(savedFolder);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/full")
    public ResponseEntity<?> getFullDetail(@PathVariable Long id, Authentication auth) {
        try {
            return ResponseEntity.ok(workspaceService.getWorkspaceFullDetail(id, auth.getName()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}