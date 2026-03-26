package dinhhung.mockmaster_server.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkspaceDTO {
    private Long workspaceId;
    private String workspaceName;
    private String description;
    private int folderCount;
    private String ownerId; // Chỉ lấy ID của user thay vì cả object UserAccount
}