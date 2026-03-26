package dinhhung.mockmaster_server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser; // Thêm import này
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin", roles = {"ADMIN"}) // Giả lập user có quyền ADMIN cho tất cả test
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Admin: Lấy thống kê và kiểm tra doanh thu từ DataInitializer")
    void getStats_Success() throws Exception {
        mockMvc.perform(get("/api/admin/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(100000.0));
    }

    @Test
    @DisplayName("Admin: Cập nhật gói PREMIUM cho hung_dev thành công")
    void updatePlan_Success() throws Exception {
        mockMvc.perform(patch("/api/admin/users/hung_dev/plan")
                        .param("planName", "PREMIUM")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Cập nhật Plan thành công cho user: hung_dev"));
    }

    @Test
    @DisplayName("Admin: Khóa tài khoản hung_dev và kiểm tra phản hồi")
    void updateStatus_LockUser_Success() throws Exception {
        mockMvc.perform(patch("/api/admin/users/hung_dev/status")
                        .param("status", "LOCKED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Cập nhật trạng thái thành công cho user: hung_dev"));
    }

    @Test
    @DisplayName("Admin: Trả về 400 khi User không tồn tại")
    void updateStatus_UserNotFound() throws Exception {
        mockMvc.perform(patch("/api/admin/users/non_existent_id/status")
                        .param("status", "LOCKED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found: non_existent_id"));
    }
}