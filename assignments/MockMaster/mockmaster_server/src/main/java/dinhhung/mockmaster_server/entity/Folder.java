package dinhhung.mockmaster_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "Folder")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folderId;

    @Column(nullable = false)
    private String folderName;

    @ManyToOne
    @JoinColumn(name = "WorkspaceID")
    @JsonIgnore
    private Workspace workspace;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<MockEndpoint> endpoints;
}