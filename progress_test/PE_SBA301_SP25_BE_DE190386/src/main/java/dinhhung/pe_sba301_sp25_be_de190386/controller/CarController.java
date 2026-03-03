package dinhhung.pe_sba301_sp25_be_de190386.controller;

import dinhhung.pe_sba301_sp25_be_de190386.dto.response.CarResponse;
import dinhhung.pe_sba301_sp25_be_de190386.entity.Car;
import dinhhung.pe_sba301_sp25_be_de190386.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    // 1. List all cars (No permissions required)
    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    // 2. Delete car (Require Admin role)
    // Giả sử trong hệ thống của bạn, Role Admin được map là 'ROLE_1'
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('1')")
    public ResponseEntity<String> deleteCar(@PathVariable Integer id) {
        carService.deleteCar(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // 3. Add new car (Require Admin role)
    @PostMapping
    @PreAuthorize("hasRole('1')")
    public ResponseEntity<?> addCar(@RequestBody Car car) {
        try {
            Car savedCar = carService.saveCar(car);
            return ResponseEntity.ok(savedCar);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('1')")
    public ResponseEntity<?> updateCar(@PathVariable Integer id, @RequestBody Car car) {
        try {
            System.out.println("Updating car ...");
            carService.updateCar(id, car);
            return ResponseEntity.ok().body("{\"message\": \"Update successfully\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}