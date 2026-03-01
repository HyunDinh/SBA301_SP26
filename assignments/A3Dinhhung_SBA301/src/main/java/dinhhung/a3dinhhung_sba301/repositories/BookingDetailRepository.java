package dinhhung.a3dinhhung_sba301.repositories;

import dinhhung.a3dinhhung_sba301.entities.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Long> {

    /**
     * Kiểm tra xem RoomID có tồn tại trong bất kỳ giao dịch thuê phòng nào không.
     * Tên hàm tuân thủ quy tắc đặt tên của Spring Data JPA:
     * exists + By + [Tên biến trong Entity BookingDetail] + _ + [Tên biến ID trong Entity Room]
     */
    boolean existsByRoom_RoomId(Long roomId);
}