package dinhhung.pe_sba301_sp25_be_de190386.repository;

import dinhhung.pe_sba301_sp25_be_de190386.entity.AccountMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountMemberRepository extends JpaRepository<AccountMember, String> {
    Optional<AccountMember> findByEmailAddress(String email);
}