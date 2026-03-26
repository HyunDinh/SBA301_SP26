package dinhhung.mockmaster_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "UserAccount")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserAccount {
    @Id
    @Column(name = "UserID")
    // Nếu bạn muốn dùng String (như Firebase UID) thì giữ nguyên,
    // còn nếu muốn DB tự sinh chuỗi ngẫu nhiên thì thêm @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String role; // ADMIN, USER
    private String status; // ACTIVE, LOCKED

    @Column(unique = true, length = 15)
    private String systemDomain;

    @ManyToOne
    @JoinColumn(name = "PlanID")
    @JsonIgnore
    private SubscriptionPlan subscriptionPlan;

    // SỬA: mappedBy phải là "user" vì trong Workspace.java bạn đặt tên biến là private UserAccount user;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Workspace> workspaces;
}