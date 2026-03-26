package dinhhung.mockmaster_server.service;

import dinhhung.mockmaster_server.entity.PaymentTransaction;
import dinhhung.mockmaster_server.entity.SubscriptionPlan;
import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.repository.PaymentTransactionRepository;
import dinhhung.mockmaster_server.repository.SubscriptionPlanRepository;
import dinhhung.mockmaster_server.repository.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentTransactionRepository transactionRepository;
    private final UserAccountRepository userRepository;
    private final SubscriptionPlanRepository planRepository;

    private final String vnp_TmnCode = "PMJBU7GD";
    private final String vnp_HashSecret = "A81XDQ704VZ2VEZCVD1V2A9W5AQTJ87D";
    private final String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private final String vnp_ReturnUrl = "http://localhost:5173/payment-result";

    public String createPaymentUrl(String userId, HttpServletRequest request) {
        UserAccount user = userRepository.findById(userId).orElseThrow();
        SubscriptionPlan premiumPlan = planRepository.findByPlanName("PREMIUM").orElseThrow();

        String vnp_TxnRef = UUID.randomUUID().toString();
        long amount = (long) (premiumPlan.getPrice() * 100);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Nâng cấp Premium cho: " + userId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", request.getRemoteAddr());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(LocalDateTime.now()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) { query.append('&'); hashData.append('&'); }
            }
        }

        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        String queryUrl = query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setVnpTxnRef(vnp_TxnRef);
        transaction.setAmount(premiumPlan.getPrice());
        transaction.setStatus("PENDING");
        transaction.setUser(user);
        transactionRepository.save(transaction);

        return vnp_PayUrl + "?" + queryUrl;
    }

    @Transactional
    public void processPaymentReturn(Map<String, String> params) {
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TxnRef = params.get("vnp_TxnRef");
        PaymentTransaction transaction = transactionRepository.findByVnpTxnRef(vnp_TxnRef).orElseThrow();

        if ("00".equals(vnp_ResponseCode)) {
            transaction.setStatus("SUCCESS");
            transaction.setPaymentDate(LocalDateTime.now());
            UserAccount user = transaction.getUser();
            SubscriptionPlan premiumPlan = planRepository.findByPlanName("PREMIUM").orElseThrow();
            user.setSubscriptionPlan(premiumPlan); // Tự động nâng cấp!
            userRepository.save(user);
        } else {
            transaction.setStatus("FAILED");
        }
        transactionRepository.save(transaction);
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512_HMAC.init(secret_key);
            byte[] bytes = sha512_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) hash.append(String.format("%02x", b));
            return hash.toString();
        } catch (Exception e) { return null; }
    }
}