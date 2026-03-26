package dinhhung.mockmaster_server.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserWorkspaceDetailDTO {
    private String workspaceName;
    private int apiCount;
}