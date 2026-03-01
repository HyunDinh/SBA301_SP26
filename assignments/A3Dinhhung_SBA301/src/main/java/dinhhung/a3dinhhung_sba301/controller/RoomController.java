package dinhhung.a3dinhhung_sba301.controller;

import dinhhung.a3dinhhung_sba301.entities.RoomInformation;
import dinhhung.a3dinhhung_sba301.repositories.RoomRepository;
import dinhhung.a3dinhhung_sba301.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class RoomController {

    @Autowired
    private RoomRepository roomRepo;
    @Autowired private RoomService roomService;

    // Khách xem phòng (Public)
    @GetMapping("/rooms")
    public List<RoomInformation> getAllRooms() {
        return roomRepo.findAll();
    }

    // Nhân viên quản lý phòng (Staff only)
    @PostMapping("/staff/rooms")
    public RoomInformation createRoom(@RequestBody RoomInformation room) {
        return roomRepo.save(room);
    }

    @DeleteMapping("/staff/rooms/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id); // Gọi logic xóa thông minh đã viết ở bước trước
        return ResponseEntity.ok("Processed successfully");
    }
}