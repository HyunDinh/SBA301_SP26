package dinhhung.pe_sba301_sp25_be_de190386.service;

import dinhhung.pe_sba301_sp25_be_de190386.dto.response.CarResponse;
import dinhhung.pe_sba301_sp25_be_de190386.entity.Car;
import dinhhung.pe_sba301_sp25_be_de190386.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    // Task 2.1: List all cars
    @Transactional(readOnly = true)
    public List<CarResponse> getAllCars() {
        // Sắp xếp ID giảm dần để item mới lên top (cho Task 2.3)
        return carRepository.findAll(Sort.by(Sort.Direction.DESC, "carId"))
                .stream()
                .map(car -> new CarResponse(
                        car.getCarId(), car.getCarName(), car.getUnitsInStock(),
                        car.getUnitPrice(), car.getCountry().getCountryName(),
                        car.getCreatedAt(), car.getUpdatedAt()
                )).collect(Collectors.toList());
    }

    // Task 2.2: Delete car
    public void deleteCar(Integer id) {
        carRepository.deleteById(id);
    }

    // Task 2.3: Add new car with validations
    public Car saveCar(Car car) {
        // Validation logic
        if (car.getCarName() == null || car.getCarName().length() <= 10) {
            throw new RuntimeException("CarName must be > 10 characters");
        }
        if (car.getUnitsInStock() < 5 || car.getUnitsInStock() > 20) {
            throw new RuntimeException("UnitsInStock must be between 5 and 20");
        }

        car.setCreatedAt(LocalDateTime.now());
        if (car.getUpdatedAt() != null && car.getCreatedAt().isAfter(car.getUpdatedAt())) {
            throw new RuntimeException("CreatedAt must be <= UpdatedAt");
        }

        return carRepository.save(car);
    }

    public Car updateCar(Integer id, Car carDetails) {
        // 1. Kiểm tra xe có tồn tại không
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));

        // 2. Chạy Validation (Dùng lại logic của bạn)
        if (carDetails.getCarName() == null || carDetails.getCarName().length() <= 10) {
            throw new RuntimeException("CarName must be > 10 characters");
        }
        if (carDetails.getUnitsInStock() < 5 || carDetails.getUnitsInStock() > 20) {
            throw new RuntimeException("UnitsInStock must be between 5 and 20");
        }

        // 3. Cập nhật thông tin
        existingCar.setCarName(carDetails.getCarName());
        existingCar.setUnitsInStock(carDetails.getUnitsInStock());
        existingCar.setUnitPrice(carDetails.getUnitPrice());
        existingCar.setCountry(carDetails.getCountry());
        existingCar.setUpdatedAt(LocalDateTime.now()); // Chỉ cập nhật ngày sửa

        return carRepository.save(existingCar);
    }
}