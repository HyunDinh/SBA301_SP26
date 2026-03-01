package dinhhung.a3dinhhung_sba301.controller;

import dinhhung.a3dinhhung_sba301.dto.LoginRequest;
import dinhhung.a3dinhhung_sba301.entities.Customer;
import dinhhung.a3dinhhung_sba301.repositories.CustomerRepository;
import dinhhung.a3dinhhung_sba301.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired
    private CustomerRepository customerRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest);
            return ResponseEntity.ok(Map.of("token", token, "role", token.contains("STAFF") ? "STAFF" : "CUSTOMER"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Customer customer) {
        customer.setCustomerStatus(1); // Active mặc định
        return ResponseEntity.ok(customerRepo.save(customer));
    }
}