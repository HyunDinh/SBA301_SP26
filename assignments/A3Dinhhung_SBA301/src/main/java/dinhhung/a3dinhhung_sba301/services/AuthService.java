package dinhhung.a3dinhhung_sba301.services;

import dinhhung.a3dinhhung_sba301.dto.LoginRequest;
import dinhhung.a3dinhhung_sba301.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Value("${app.staff.email}") private String staffEmail;
    @Value("${app.staff.password}") private String staffPass;
    @Autowired
    private CustomerRepository customerRepository;

    public String login(LoginRequest req) {
        // 1. Kiểm tra nếu là Staff
        if (req.getEmail().equals(staffEmail) && req.getPassword().equals(staffPass)) {
            return "FAKE_JWT_TOKEN_STAFF"; // Thay bằng JWT thật sau
        }
        // 2. Kiểm tra nếu là Customer
        return customerRepository.findByEmailAddress(req.getEmail())
                .filter(c -> c.getPassword().equals(req.getPassword()))
                .map(c -> "FAKE_JWT_TOKEN_CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Unauthorized"));
    }
}