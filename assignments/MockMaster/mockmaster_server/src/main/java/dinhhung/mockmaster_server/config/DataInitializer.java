package dinhhung.mockmaster_server.config;

import dinhhung.mockmaster_server.entity.*;
import dinhhung.mockmaster_server.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userRepository;
    private final SubscriptionPlanRepository planRepository;
    private final WorkspaceRepository workspaceRepository;
    private final FolderRepository folderRepository;
    private final MockEndpointRepository endpointRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Khởi tạo Gói cước (SubscriptionPlan)
        if (planRepository.count() == 0) {
            // FREE: 2 Workspace, 15 APIs/Workspace, Giá 0
            planRepository.save(new SubscriptionPlan(null, "FREE", 2, 15, 0.0, null));
            // PREMIUM: 100 Workspace, 1000 APIs/Workspace, Giá 100k
            planRepository.save(new SubscriptionPlan(null, "PREMIUM", 100, 1000, 100000.0, null));
        }

        final SubscriptionPlan freePlan = planRepository.findByPlanName("FREE").orElse(null);
        final SubscriptionPlan premiumPlan = planRepository.findByPlanName("PREMIUM").orElse(null);

        // 2. Tạo tài khoản ADMIN (Chỉ quản trị, không có Workspace/API)
        if (userRepository.findById("admin_root").isEmpty()) {
            UserAccount admin = UserAccount.builder()
                    .userId("admin_root")
                    .email("admin@mockmaster.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role("ADMIN")
                    .status("ACTIVE")
                    .systemDomain("admin-console")
                    .subscriptionPlan(premiumPlan)
                    .build();
            userRepository.save(admin);
        }

        // 3. Tạo USER 1: Dự án Hệ thống Nhà Sách (15 APIs)
        initUserProject(
                "hung_dev", "hung@mail.com", freePlan, "hung-store-api",
                "Bookstore Pro System", "Hệ thống quản lý bán sách và vận chuyển toàn diện",
                new Object[][]{
                        {"auth", "POST", "/login", 200, "{\"token\":\"jwt.hung.secure\", \"user\":\"Hung Dinh\"}", 0},
                        {"auth", "POST", "/register", 201, "{\"message\":\"Account created successfully\"}", 0},
                        {"auth", "GET", "/profile", 200, "{\"id\":101, \"email\":\"hung@gmail.com\", \"loyalty\":\"Gold\"}", 100},
                        {"books", "GET", "/all", 200, "[{\"id\":1,\"title\":\"Clean Code\",\"price\":250000},{\"id\":2,\"title\":\"Java Core\",\"price\":180000}]", 200},
                        {"books", "GET", "/search?q=spring", 200, "{\"results\":[{\"id\":3,\"title\":\"Spring in Action\"}]}", 0},
                        {"books", "POST", "/add", 403, "{\"error\":\"Access Denied: Admin role required\"}", 0},
                        {"cart", "POST", "/add-item", 200, "{\"cartId\":55, \"totalItems\":3}", 0},
                        {"cart", "GET", "/view", 200, "{\"items\":[{\"id\":1, \"qty\":1}], \"total\":250000}", 300},
                        {"orders", "POST", "/checkout", 201, "{\"orderId\":\"ORD-BK-999\", \"status\":\"PENDING\"}", 1500},
                        {"orders", "GET", "/history", 200, "{\"orders\":[{\"id\":\"ORD-BK-998\", \"total\":500000}]}", 0},
                        {"shipping", "GET", "/track/ORD-BK-999", 200, "{\"status\":\"SHIPPING\", \"carrier\":\"GHTK\"}", 0},
                        {"inventory", "GET", "/check-stock/1", 200, "{\"inStock\": true, \"quantity\": 42}", 0},
                        {"marketing", "GET", "/vouchers", 200, "{\"codes\":[\"GIAM20K\", \"FREESHIP\"]}", 0},
                        {"reviews", "POST", "/submit", 201, "{\"reviewId\": 77, \"status\":\"Awaiting Approval\"}", 0},
                        {"settings", "PATCH", "/notify", 200, "{\"email_notify\": true}", 0}
                }
        );

        // 4. Tạo USER 2: Dự án Bãi Xe Thông Minh (10 APIs)
        initUserProject(
                "partner_parking", "hung2@mail.com", freePlan, "smart-park-88",
                "Smart Parking Pro", "Hệ thống quản lý vào/ra và thanh toán bãi đỗ xe",
                new Object[][]{
                        {"gate", "POST", "/check-in", 201, "{\"ticket\":\"T-202\", \"entryTime\":\"2026-03-16T14:30\"}", 0},
                        {"gate", "POST", "/check-out", 200, "{\"fee\": 25000, \"currency\":\"VND\"}", 200},
                        {"monitoring", "GET", "/status", 200, "{\"freeSlots\": 15, \"total\": 100, \"isFull\": false}", 0},
                        {"monitoring", "GET", "/cameras", 200, "{\"cam01\":\"ONLINE\", \"cam02\":\"ONLINE\"}", 0},
                        {"payment", "POST", "/vnpay-callback", 200, "{\"code\":\"00\", \"message\":\"Transaction Success\"}", 0},
                        {"payment", "GET", "/tariffs", 200, "{\"bike\":5000, \"car\":20000, \"truck\":50000}", 0},
                        {"tickets", "GET", "/verify/T-202", 200, "{\"isValid\": true, \"duration\":\"2h 15m\"}", 0},
                        {"tickets", "DELETE", "/void/T-202", 200, "{\"message\":\"Ticket voided\"}", 0},
                        {"staff", "GET", "/on-duty", 200, "{\"staffName\":\"Nguyen Van A\", \"shift\":\"Afternoon\"}", 0},
                        {"system", "GET", "/health-check", 200, "{\"database\":\"CONNECTED\", \"api_status\":\"UP\"}", 0}
                }
        );

        // 5. Nạp Giao dịch thanh toán mẫu (PaymentTransaction) cho User 1
        if (transactionRepository.count() == 0) {
            userRepository.findById("hung_dev").ifPresent(user -> {
                transactionRepository.save(new PaymentTransaction(null, "VNP12345678", 100000.0, "SUCCESS", LocalDateTime.now().minusDays(1), user));
                transactionRepository.save(new PaymentTransaction(null, "VNP99999999", 50000.0, "FAILED", LocalDateTime.now(), user));
            });
        }
    }

    private void initUserProject(String id, String email, SubscriptionPlan plan, String domain,
                                 String wsName, String wsDesc, Object[][] apiSet) {

        if (userRepository.findById(id).isPresent()) return;

        // Tạo User
        UserAccount user = UserAccount.builder()
                .userId(id).email(email).password(passwordEncoder.encode("user123"))
                .role("USER").status("ACTIVE").systemDomain(domain).subscriptionPlan(plan)
                .build();
        final UserAccount savedUser = userRepository.save(user);

        // Tạo Workspace - Dùng final để Lambda không báo lỗi
        Workspace ws = new Workspace(null, wsName, wsDesc, savedUser, new ArrayList<>());
        final Workspace finalWs = workspaceRepository.save(ws);

        final Map<String, Folder> folderMap = new HashMap<>();

        for (Object[] api : apiSet) {
            String folderName = (String) api[0];

            // Lấy hoặc tạo mới Folder (Lambda truy cập finalWs)
            Folder folder = folderMap.computeIfAbsent(folderName, name -> {
                Folder f = new Folder(null, name, finalWs, new ArrayList<>());
                return folderRepository.save(f);
            });

            // Tạo MockEndpoint
            MockEndpoint ep = new MockEndpoint();
            ep.setMethod((String) api[1]);
            ep.setPath((String) api[2]);
            ep.setStatusCode((Integer) api[3]);
            ep.setResponseBody((String) api[4]);
            ep.setContentType("application/json");
            ep.setDelayMs((Integer) api[5]);
            ep.setFolder(folder);

            endpointRepository.save(ep);
        }
    }
}


/*

NCB

9704198526191432198

NGUYEN VAN A

07/15

123456

 */