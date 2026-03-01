package dinhhung.a3dinhhung_sba301.repositories;

import dinhhung.a3dinhhung_sba301.entities.BookingReservation;
import dinhhung.a3dinhhung_sba301.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingReservation, Long> {
    List<BookingReservation> findByCustomerOrderByBookingDateDesc(Customer customer);
}