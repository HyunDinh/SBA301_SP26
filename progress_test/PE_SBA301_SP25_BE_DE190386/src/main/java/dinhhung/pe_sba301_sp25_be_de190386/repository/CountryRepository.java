package dinhhung.pe_sba301_sp25_be_de190386.repository;

import dinhhung.pe_sba301_sp25_be_de190386.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
    // JpaRepository đã cung cấp sẵn các hàm cơ bản: save, delete, findById...
}