package dinhhung.pe_sba301_sp25_be_de190386.controller;

import dinhhung.pe_sba301_sp25_be_de190386.config.JwtUtils;
import dinhhung.pe_sba301_sp25_be_de190386.entity.AccountMember;
import dinhhung.pe_sba301_sp25_be_de190386.repository.AccountMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AccountMemberRepository memberRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        AccountMember member = memberRepo.findByEmailAddress(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, member.getMemberPassword())) {
            // Tạo token thật chứa Email và Role
            String token = jwtUtils.generateToken(member.getEmailAddress(), member.getMemberRole());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", member.getMemberRole(),
                    "email", member.getEmailAddress()
            ));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}