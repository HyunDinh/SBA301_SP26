package dinhhung.pe_sba301_sp25_be_de190386.config;

import dinhhung.pe_sba301_sp25_be_de190386.entity.*;
import dinhhung.pe_sba301_sp25_be_de190386.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(AccountMemberRepository memberRepo,
                                   CountryRepository countryRepo,
                                   CarRepository carRepo) {
        return args -> {
            // 1. Thêm dữ liệu AccountMember
            memberRepo.saveAll(List.of(
                    new AccountMember("PS0001", passwordEncoder.encode("@1"), "admin@cinestar.com", 1),
                    new AccountMember("PS0002", passwordEncoder.encode("@2"), "staff@cinestar.com", 2),
                    new AccountMember("PS0003", passwordEncoder.encode("@3"), "member1@cinestar.com", 3),
                    new AccountMember("PS0004", passwordEncoder.encode("@3"), "member2@cinestar.com", 3)
            ));

            // 2. Thêm dữ liệu Country
            Country japan = new Country("1", "Japan", null);
            Country usa = new Country("2", "USA", null);
            Country france = new Country("3", "France", null);
            Country germany = new Country("4", "Germany", null);
            countryRepo.saveAll(List.of(japan, usa, france, germany));

            // 3. Thêm dữ liệu Cars (Khớp đúng CountryID theo hình)
            carRepo.saveAll(List.of(
                    new Car(null, "Honda CV", 12, 18000.0, LocalDate.parse("2025-01-01").atStartOfDay(), LocalDate.parse("2025-01-02").atStartOfDay(), japan),
                    new Car(null, "Camry", 23, 19000.0, LocalDate.parse("2025-01-01").atStartOfDay(), LocalDate.parse("2025-01-02").atStartOfDay(), japan),
                    new Car(null, "Mercedes", 10, 35000.0, LocalDate.parse("2025-01-01").atStartOfDay(), LocalDate.parse("2025-01-02").atStartOfDay(), germany),
                    new Car(null, "Ford Everest", 20, 40000.0, LocalDate.parse("2025-01-01").atStartOfDay(), LocalDate.parse("2025-01-02").atStartOfDay(), usa),
                    new Car(null, "Lexus", 10, 90000.0, LocalDate.parse("2025-01-01").atStartOfDay(), LocalDate.parse("2025-01-01").atStartOfDay(), usa),
                    new Car(null, "Peugeot 3008", 10, 91000.0, LocalDate.parse("2025-01-01").atStartOfDay(), LocalDate.parse("2025-01-01").atStartOfDay(), france)
            ));

            System.out.println("--- Dữ liệu mẫu đã được khởi tạo thành công! ---");
        };
    }
}