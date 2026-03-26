package dinhhung.mockmaster_server.service;

import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAccountRepository userRepository;

    // Lấy thông tin chi tiết user
    @Transactional(readOnly = true)
    public UserAccount getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
    }

    // Logic Reset System Domain (Unique, 15 ký tự)
    @Transactional
    public String resetSystemDomain(String userId) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        String newDomain;
        // Vòng lặp đảm bảo domain tạo ra là duy nhất trong hệ thống
        do {
            newDomain = generateRandomDomain(15);
        } while (userRepository.existsBySystemDomain(newDomain));

        user.setSystemDomain(newDomain);
        userRepository.save(user);

        return newDomain;
    }

    // Helper tạo chuỗi ngẫu nhiên 15 ký tự
    private String generateRandomDomain(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}