package dinhhung.mockmaster_server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Khởi tạo ObjectMapper thủ công để tránh lỗi Bean đỏ trong IntelliJ
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "hung_dev") // Giả lập user 'hung_dev' đã đăng nhập
    @DisplayName("Test API lấy Profile: Trả về đúng thông tin user")
    void getProfile_Success() throws Exception {
        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("hung_dev"))
                .andExpect(jsonPath("$.email").value("hung@mail.com"))
                .andExpect(jsonPath("$.systemDomain").exists())
                .andExpect(jsonPath("$.planName").value("FREE"));
    }

    @Test
    @WithMockUser(username = "hung_dev")
    @DisplayName("Test API Reset Domain: Kiểm tra domain mới có đúng 15 ký tự")
    void resetDomain_Success() throws Exception {
        mockMvc.perform(post("/api/users/reset-domain")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reset domain thành công"))
                // Kiểm tra logic 15 ký tự mà Hùng đã viết trong UserService
                .andExpect(jsonPath("$.systemDomain", hasLength(15)))
                // Kiểm tra domain mới chỉ chứa chữ thường và số
                .andExpect(jsonPath("$.systemDomain", matchesPattern("^[a-z0-9]+$")));
    }

    @Test
    @DisplayName("Test bảo mật: Truy cập Profile không có Token sẽ bị chặn (401/403)")
    void getProfile_NoAuth_ShouldFail() throws Exception {
        mockMvc.perform(get("/api/users/profile"))
                // Tùy vào cấu hình Security của bạn, thường là 401 Unauthorized hoặc 403 Forbidden
                .andExpect(status().isForbidden());
    }
}