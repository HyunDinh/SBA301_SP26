package dinhhung.mockmaster_server.dto;

import lombok.*;
import java.util.List;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class WorkspaceFullDTO {
    private Long workspaceId;
    private String workspaceName;
    private String description;
    private List<FolderDTO> folders;
}