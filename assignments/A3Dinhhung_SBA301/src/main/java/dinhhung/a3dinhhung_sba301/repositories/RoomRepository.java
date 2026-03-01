package dinhhung.a3dinhhung_sba301.repositories;

import dinhhung.a3dinhhung_sba301.entities.RoomInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<RoomInformation, Long> {
    List<RoomInformation> findByRoomStatus(Integer status);
}