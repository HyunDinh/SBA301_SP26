package dinhhung.mockmaster_server.controller;

import dinhhung.mockmaster_server.dto.MockEndpointDTO;
import dinhhung.mockmaster_server.service.MockEndpointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/endpoints")
@RequiredArgsConstructor
public class MockEndpointController {

    private final MockEndpointService endpointService;

    // 1. TẠO MỚI (Dùng hàm trong Service để kiểm tra quyền)
    @PostMapping("/folder/{folderId}")
    public ResponseEntity<?> createEndpoint(@PathVariable Long folderId,
                                            @RequestBody MockEndpointDTO dto,
                                            Authentication auth) {
        return ResponseEntity.ok(endpointService.createEndpoint(folderId, dto, auth.getName()));
    }

    // 2. CẬP NHẬT (Hàm này Hùng cần thêm vào nè!)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEndpoint(@PathVariable Long id,
                                            @RequestBody MockEndpointDTO dto,
                                            Authentication auth) {
        // Bạn có thể gọi trực tiếp repo hoặc tốt nhất là đưa vào Service
        // để check xem user có quyền sửa endpoint này không (giống hàm delete)
        return ResponseEntity.ok(endpointService.updateEndpoint(id, dto, auth.getName()));
    }

    // 3. XÓA
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEndpoint(@PathVariable Long id, Authentication auth) {
        endpointService.deleteEndpoint(id, auth.getName());
        return ResponseEntity.ok("Xóa endpoint thành công");
    }
}