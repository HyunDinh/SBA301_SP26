package dinhhung.mockmaster_server;

import com.fasterxml.jackson.databind.ObjectMapper;
import dinhhung.mockmaster_server.entity.Workspace;
import dinhhung.mockmaster_server.service.WorkspaceService; // Thêm Service để tạo data mồi
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional; // Import cái thứ 2 như đã chọn

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Cực kỳ quan trọng: Để sau khi test xong, 2 cái workspace mồi sẽ bị xóa khỏi DB
public class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkspaceService workspaceService; // Tiêm service để chuẩn bị dữ liệu

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "hung_dev")
    @DisplayName("Lấy danh sách Workspace của User thành công")
    void getAll_Success() throws Exception {
        mockMvc.perform(get("/api/workspaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "hung_dev")
    @DisplayName("Tạo Workspace thất bại khi vượt quá giới hạn gói FREE (2 WS)")
    void create_LimitReached() throws Exception {
        // 1. Dọn dẹp hoặc chuẩn bị: Tạo cho đủ 2 cái (giả định gói FREE limit = 2)
        Workspace dummy1 = new Workspace();
        dummy1.setWorkspaceName("Dummy 1");

        Workspace dummy2 = new Workspace();
        dummy2.setWorkspaceName("Dummy 2");

        // Gọi trực tiếp service để lấp đầy slot (không dùng MockMvc để nhanh hơn)
        // Lưu ý: Nếu DataInitializer đã tạo 1 cái, thì ở đây chỉ cần tạo thêm 1 cái nữa là đủ.
        // Để an toàn, cứ tạo đến khi nào Service ném lỗi hoặc tạo đủ 2 cái.
        try {
            workspaceService.createWorkspace("hung_dev", dummy1);
            workspaceService.createWorkspace("hung_dev", dummy2);
        } catch (Exception e) {
            // Nếu đã đầy từ trước thì thôi, không sao cả
        }

        // 2. Bây giờ mới dùng MockMvc để tạo cái THỨ 3 -> Chắc chắn phải FAIL (400)
        Workspace wsFail = new Workspace();
        wsFail.setWorkspaceName("Cái này phải lỗi");

        mockMvc.perform(post("/api/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wsFail)))
                .andExpect(status().isBadRequest()) // Mong đợi 400
                .andExpect(jsonPath("$.message").value(containsString("PAYMENT_REQUIRED")));
    }
}