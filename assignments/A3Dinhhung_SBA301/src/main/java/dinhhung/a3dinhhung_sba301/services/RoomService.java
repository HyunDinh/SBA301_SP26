package dinhhung.a3dinhhung_sba301.services;
import dinhhung.a3dinhhung_sba301.entities.RoomInformation;
import dinhhung.a3dinhhung_sba301.repositories.BookingDetailRepository;
import dinhhung.a3dinhhung_sba301.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private BookingDetailRepository bookingDetailRepo;

    public void deleteRoom(Long roomId) {
        // Kiểm tra xem phòng này đã từng có ai đặt (renting transaction) chưa
        boolean isUsed = bookingDetailRepo.existsByRoom_RoomId(roomId);

        if (isUsed) {
            // Nếu đã có giao dịch: Chỉ chuyển trạng thái (Status)
            RoomInformation room = roomRepo.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            room.setRoomStatus(0); // Giả sử 0 là Inactive/Deleted
            roomRepo.save(room);
        } else {
            // Nếu chưa từng có giao dịch: Xóa hẳn khỏi Database
            roomRepo.deleteById(roomId);
        }
    }
}