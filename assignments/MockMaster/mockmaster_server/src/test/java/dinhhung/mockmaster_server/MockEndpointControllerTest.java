package dinhhung.mockmaster_server;

import com.fasterxml.jackson.databind.ObjectMapper;
import dinhhung.mockmaster_server.dto.MockEndpointDTO;
import dinhhung.mockmaster_server.entity.Folder;
import dinhhung.mockmaster_server.repository.FolderRepository;
import dinhhung.mockmaster_server.repository.MockEndpointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MockEndpointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockEndpointRepository endpointRepository;

    @Autowired
    private FolderRepository folderRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(username = "hung_dev")
    @DisplayName("Chặn tính năng Delay nếu không phải tài khoản PREMIUM")
    void createEndpoint_Fail_DelayNotAllowed() throws Exception {
        // 1. CHUẨN BỊ: Lấy một folder bất kỳ của hung_dev
        Folder folder = folderRepository.findAll().stream()
                .filter(f -> f.getWorkspace().getUser().getUserId().equals("hung_dev"))
                .findFirst()
                .orElseThrow();

        Long folderId = folder.getFolderId();
        Long workspaceId = folder.getWorkspace().getWorkspaceId();

        // 2. QUAN TRỌNG: Xóa sạch API cũ để không bị dính lỗi "Đạt giới hạn 15 API"
        endpointRepository.deleteAllByWorkspaceId(workspaceId);

        // 3. THỰC THI: Tạo API mới có DelayMs
        MockEndpointDTO dto = MockEndpointDTO.builder()
                .path("/api/test-premium-feature")
                .method("GET")
                .delayMs(1000) // 1 giây delay (Premium only)
                .statusCode(200)
                .contentType("application/json")
                .build();

        // Bắt ServletException
        jakarta.servlet.ServletException exception = assertThrows(jakarta.servlet.ServletException.class, () -> {
            mockMvc.perform(post("/api/endpoints/folder/" + folderId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)));
        });

        // 4. KIỂM TRA: Lúc này lỗi trả về chắc chắn phải là lỗi Premium Delay
        Throwable rootCause = exception.getCause();
        assertNotNull(rootCause);

        String actualMessage = rootCause.getMessage();
        System.out.println("Actual Error Message: " + actualMessage);

        assertTrue(actualMessage.contains("chỉ dành cho tài khoản PREMIUM"),
                "Lẽ ra phải lỗi Premium Delay nhưng lại bị lỗi: " + actualMessage);
    }
}