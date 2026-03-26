package dinhhung.mockmaster_server.controller;

import dinhhung.mockmaster_server.dto.AdminDashboardStats;
import dinhhung.mockmaster_server.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<AdminDashboardStats> getDashboardStats() {
        return ResponseEntity.ok(adminService.getStats());
    }

    // API để admin cập nhật Plan thủ công (ví dụ nâng cấp lên PREMIUM cho user)
    @PatchMapping("/users/{userId}/plan")
    public ResponseEntity<?> updatePlan(@PathVariable String userId, @RequestParam String planName) {
        try {
            adminService.updateUserPlan(userId, planName);
            return ResponseEntity.ok("Cập nhật Plan thành công cho user: " + userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API để admin khóa/mở khóa user
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String userId, @RequestParam String status) {
        try {
            adminService.updateUserStatus(userId, status);
            return ResponseEntity.ok("Cập nhật trạng thái thành công cho user: " + userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}