package dinhhung.mockmaster_server;

import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.entity.Workspace;
import dinhhung.mockmaster_server.repository.UserAccountRepository;
import dinhhung.mockmaster_server.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MockEngineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    private String systemDomain;
    private Long workspaceId;

    @BeforeEach
    void setUp() {
        // Lấy User từ DataInitializer (hung_dev)
        UserAccount user = userRepository.findById("hung_dev")
                .orElseThrow(() -> new RuntimeException("DataInitializer chưa chạy!"));

        this.systemDomain = user.getSystemDomain();

        // Lấy đúng WorkspaceId của User này
        List<Workspace> workspaces = workspaceRepository.findByUser_UserId("hung_dev");
        this.workspaceId = workspaces.get(0).getWorkspaceId();
    }

    @Test
    @DisplayName("Engine: Lấy danh sách sách thành công (Path khớp DB: /all)")
    void handleRequest_GetBooks_Success() throws Exception {
        // Lưu ý: Trong DataInitializer, path là "/all", không phải "/books/all"
        mockMvc.perform(get("/mock/" + systemDomain + "/" + workspaceId + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test
    @DisplayName("Mock Engine: Lấy Profile (Path khớp DB: /profile)")
    void handleRequest_GetProfile_Success() throws Exception {
        // Path trong DB là "/profile"
        mockMvc.perform(get("/mock/" + systemDomain + "/" + workspaceId + "/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("hung@gmail.com"));
    }

    @Test
    @DisplayName("Engine: Test lỗi 403 (Phải dùng POST và đúng Path /add)")
    void handleRequest_MockedError_403() throws Exception {
        // DataInitializer: {"books", "POST", "/add", 403, ...}
        // Phải dùng post() mới khớp Method trong DB

        mockMvc.perform(post("/mock/" + systemDomain + "/" + workspaceId + "/add"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Access Denied: Admin role required"));
    }

    @Test
    @DisplayName("Mock Engine: Trả về lỗi 404 khi Path không tồn tại")
    void handleRequest_PathNotFound_404() throws Exception {
        mockMvc.perform(get("/mock/" + systemDomain + "/" + workspaceId + "/path-khong-ton-tai"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Mock endpoint not found"));
    }

    @Test
    @DisplayName("Mock Engine: Chặn truy cập khi Tài khoản bị khóa (LOCKED)")
    void handleRequest_UserAccountLocked_403() throws Exception {
        // Khóa user
        UserAccount user = userRepository.findById("hung_dev").get();
        user.setStatus("LOCKED");
        userRepository.save(user);

        mockMvc.perform(get("/mock/" + systemDomain + "/" + workspaceId + "/all"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error", containsString("locked")));
    }
}