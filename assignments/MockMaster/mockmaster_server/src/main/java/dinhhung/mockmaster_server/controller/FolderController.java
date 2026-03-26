package dinhhung.mockmaster_server.controller;

import dinhhung.mockmaster_server.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/workspace/{workspaceId}")
    public ResponseEntity<?> create(@PathVariable Long workspaceId,
                                    @RequestBody Map<String, String> request,
                                    Authentication auth) {
        return ResponseEntity.ok(folderService.createFolder(workspaceId, request.get("folderName"), auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Map<String, String> request,
                                    Authentication auth) {
        return ResponseEntity.ok(folderService.updateFolder(id, request.get("folderName"), auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        folderService.deleteFolder(id, auth.getName());
        return ResponseEntity.ok("Xóa folder thành công");
    }
}