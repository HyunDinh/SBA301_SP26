package dinhhung.a3dinhhung_sba301.controller;

import dinhhung.a3dinhhung_sba301.entities.BookingReservation;
import dinhhung.a3dinhhung_sba301.repositories.BookingDetailRepository;
import dinhhung.a3dinhhung_sba301.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepo;
    @Autowired private BookingDetailRepository detailRepo;

    @PostMapping("/booking")
    public ResponseEntity<?> createBooking(@RequestBody BookingReservation booking) {
        booking.setBookingDate(LocalDate.now());
        booking.setBookingStatus(1);
        return ResponseEntity.ok(bookingRepo.save(booking));
    }

    @GetMapping("/history/{customerId}")
    public List<BookingReservation> getHistory(@PathVariable Long customerId) {
        return bookingRepo.findAll();
    }
}