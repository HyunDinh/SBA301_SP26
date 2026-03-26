package dinhhung.mockmaster_server.controller;

import dinhhung.mockmaster_server.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // API này được gọi khi user nhấn "Buy Premium"
    @PostMapping("/create-url")
    public ResponseEntity<?> createUrl(Authentication auth, HttpServletRequest request) {
        // auth.getName() sẽ lấy userId từ JWT hoặc Session của Hùng
        String url = paymentService.createPaymentUrl(auth.getName(), request);
        return ResponseEntity.ok(Map.of("paymentUrl", url));
    }

    // Sau khi thanh toán xong, Frontend nhận kết quả từ VNPay rồi "đẩy" ngược các tham số về đây
    @GetMapping("/vnpay-callback")
    public ResponseEntity<?> callback(@RequestParam Map<String, String> params) {
        paymentService.processPaymentReturn(params);
        return ResponseEntity.ok(Map.of("message", "Xử lý kết quả thanh toán hoàn tất"));
    }
}