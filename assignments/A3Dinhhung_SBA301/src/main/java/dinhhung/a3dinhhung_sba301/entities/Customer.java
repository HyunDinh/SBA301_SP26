package dinhhung.a3dinhhung_sba301.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String customerFullName;
    private String telephone;
    @Column(unique = true)
    private String emailAddress;
    private LocalDate customerBirthday;
    private Integer customerStatus;
    private String password;
}