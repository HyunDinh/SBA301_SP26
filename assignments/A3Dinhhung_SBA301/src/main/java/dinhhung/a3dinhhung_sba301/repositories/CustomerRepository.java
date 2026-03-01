package dinhhung.a3dinhhung_sba301.repositories;

import dinhhung.a3dinhhung_sba301.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmailAddress(String email);
}