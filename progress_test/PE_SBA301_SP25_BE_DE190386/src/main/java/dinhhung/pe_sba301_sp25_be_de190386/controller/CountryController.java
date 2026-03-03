package dinhhung.pe_sba301_sp25_be_de190386.controller;

import dinhhung.pe_sba301_sp25_be_de190386.entity.Country;
import dinhhung.pe_sba301_sp25_be_de190386.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryRepository countryRepository;

    // Lấy danh sách quốc gia để hiển thị trong dropdown khi Add/Update Car
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        // Vì Country đơn giản, ta có thể trả về trực tiếp Entity
        // hoặc dùng DTO nếu muốn giấu field 'cars'
        return ResponseEntity.ok(countryRepository.findAll());
    }
}