package dinhhung.pe_sba301_sp25_be_de190386.repository;

import dinhhung.pe_sba301_sp25_be_de190386.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    // Bạn có thể thêm các hàm truy vấn nâng cao nếu cần, ví dụ:
    // Tìm các xe thuộc một quốc gia cụ thể
    List<Car> findByCountry_CountryId(String countryId);

    // Tìm các xe có giá trong khoảng nhất định
    List<Car> findByUnitPriceBetween(Double min, Double max);
}