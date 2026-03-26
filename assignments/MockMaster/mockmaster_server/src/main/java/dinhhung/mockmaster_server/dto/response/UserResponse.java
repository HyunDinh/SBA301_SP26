package dinhhung.mockmaster_server.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String userId;
    private String email;
    private String role;
    private String token;
    private String planName; // Để hiện thị người dùng là FREE hay PREMIUM
}