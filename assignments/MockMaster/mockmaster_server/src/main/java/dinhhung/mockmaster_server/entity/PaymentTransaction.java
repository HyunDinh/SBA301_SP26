package dinhhung.mockmaster_server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PaymentTransaction")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private String vnpTxnRef; // Mã tham chiếu VNPay
    private Double amount;
    private String status; // PENDING, SUCCESS, FAILED
    private LocalDateTime paymentDate;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private UserAccount user;
}