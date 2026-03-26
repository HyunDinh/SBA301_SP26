package dinhhung.mockmaster_server.repository;

import dinhhung.mockmaster_server.entity.UserAccount;
import dinhhung.mockmaster_server.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    List<Workspace> findByUser(UserAccount user);
    List<Workspace> findByUser_UserId(String userId);
    long countByUser_UserId(String userId);
}