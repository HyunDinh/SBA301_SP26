package dinhhung.mockmaster_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "Workspace")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workspaceId;

    @Column(nullable = false)
    private String workspaceName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "UserID")
    @JsonIgnore
    private UserAccount user;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
    private List<Folder> folders;
}