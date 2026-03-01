package dinhhung.a3dinhhung_sba301.services;

import dinhhung.a3dinhhung_sba301.entities.BookingDetail;
import dinhhung.a3dinhhung_sba301.entities.BookingReservation;
import dinhhung.a3dinhhung_sba301.repositories.BookingDetailRepository;
import dinhhung.a3dinhhung_sba301.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepo;
    @Autowired private BookingDetailRepository detailRepo;

    @Transactional
    public BookingReservation createFullBooking(BookingReservation reservation, List<BookingDetail> details) {
        // 1. Lưu hóa đơn tổng trước
        BookingReservation savedReservation = bookingRepo.save(reservation);

        // 2. Lưu từng chi tiết phòng đã chọn
        for (BookingDetail detail : details) {
            detail.setBookingReservation(savedReservation);
            detailRepo.save(detail);
        }

        return savedReservation;
    }
}