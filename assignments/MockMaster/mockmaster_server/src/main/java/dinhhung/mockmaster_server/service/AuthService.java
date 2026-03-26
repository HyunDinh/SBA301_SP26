package dinhhung.mockmaster_server.service;

import dinhhung.mockmaster_server.config.JwtUtils;
import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public Map<String, Object> login(String email, String password) {
        UserAccount user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }

        if ("LOCKED".equals(user.getStatus())) {
            throw new RuntimeException("Tài khoản đã bị khóa!");
        }

        // Tạo token với Role String trực tiếp từ DB
        String token = jwtUtils.generateToken(user.getUserId(), user.getEmail(), user.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user); // Trả về cả object user để Controller lấy thông tin plan

        return response;
    }
}