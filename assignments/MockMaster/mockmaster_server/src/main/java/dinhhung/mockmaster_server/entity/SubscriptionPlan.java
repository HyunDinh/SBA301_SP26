package dinhhung.mockmaster_server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "SubscriptionPlan")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    @Column(nullable = false)
    private String planName; // FREE, PREMIUM

    private Integer maxWorkspaces;
    private Integer maxEndpointsPerWorkspace;
    private Double price;

    @OneToMany(mappedBy = "subscriptionPlan")
    @JsonIgnore
    private List<UserAccount> users;
}