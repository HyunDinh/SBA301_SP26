package dinhhung.a3dinhhung_sba301.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}