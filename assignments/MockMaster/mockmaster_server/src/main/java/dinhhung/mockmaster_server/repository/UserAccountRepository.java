package dinhhung.mockmaster_server.repository;

import dinhhung.mockmaster_server.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    // Tìm kiếm bằng Email (Dùng cho Login/Security)
    Optional<UserAccount> findByEmail(String email);

    // Kiểm tra domain tồn tại (Dùng khi reset domain mới)
    boolean existsBySystemDomain(String systemDomain);

    // BỔ SUNG: Tìm User bằng System Domain (Dùng cho Mock Engine check status)
    Optional<UserAccount> findBySystemDomain(String systemDomain);
}