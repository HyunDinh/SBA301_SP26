package dinhhung.mockmaster_server.dto;

import lombok.*;

import java.util.List;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class FolderDTO {
    private Long folderId;
    private String folderName;
    private List<MockEndpointDTO> endpoints;
}