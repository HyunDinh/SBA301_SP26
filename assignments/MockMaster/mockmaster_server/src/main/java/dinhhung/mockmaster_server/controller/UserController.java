package dinhhung.mockmaster_server.controller;

import dinhhung.mockmaster_server.dto.response.UserResponse;
import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication auth) {
        UserAccount user = userService.getUserById(auth.getName());
        return ResponseEntity.ok(Map.of(
                "userId", user.getUserId(),
                "email", user.getEmail(),
                "role", user.getRole(),
                "systemDomain", user.getSystemDomain(),
                "planName", user.getSubscriptionPlan() != null ? user.getSubscriptionPlan().getPlanName() : "FREE"
        ));
    }

    // API Reset Domain
    @PostMapping("/reset-domain")
    public ResponseEntity<?> resetDomain(Authentication auth) {
        try {
            String newDomain = userService.resetSystemDomain(auth.getName());
            return ResponseEntity.ok(Map.of(
                    "message", "Reset domain thành công",
                    "systemDomain", newDomain
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}