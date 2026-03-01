package dinhhung.a3dinhhung_sba301.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class BookingReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingReservationId;
    private LocalDate bookingDate;
    private Double totalPrice;
    private Integer bookingStatus;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;
}