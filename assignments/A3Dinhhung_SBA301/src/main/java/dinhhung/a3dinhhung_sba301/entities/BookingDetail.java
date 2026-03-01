package dinhhung.a3dinhhung_sba301.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookingReservationId")
    private BookingReservation bookingReservation;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private RoomInformation room;

    private LocalDate startDate;
    private LocalDate endDate;
    private Double actualPrice;
}