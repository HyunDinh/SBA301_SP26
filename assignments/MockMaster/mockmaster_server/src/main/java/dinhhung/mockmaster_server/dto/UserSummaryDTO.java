package dinhhung.mockmaster_server.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserSummaryDTO {
    private String userId;
    private String email;
    private String planName;
    private int workspaceCount;
    private String status;
    private List<UserWorkspaceDetailDTO> workspaces;
}