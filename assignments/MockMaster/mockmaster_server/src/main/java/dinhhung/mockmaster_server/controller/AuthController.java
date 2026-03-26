package dinhhung.mockmaster_server.controller;

import dinhhung.mockmaster_server.dto.request.LoginRequest;
import dinhhung.mockmaster_server.dto.response.UserResponse;
import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Map<String, Object> authData = authService.login(request.getEmail(), request.getPassword());
            UserAccount user = (UserAccount) authData.get("user");

            UserResponse response = UserResponse.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .token((String) authData.get("token"))
                    .planName(user.getSubscriptionPlan() != null ? user.getSubscriptionPlan().getPlanName() : "FREE")
                    .build();

            log.info("[AuthController] Login successful with user : {}", user.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("[AuthController] Login failed ");
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}