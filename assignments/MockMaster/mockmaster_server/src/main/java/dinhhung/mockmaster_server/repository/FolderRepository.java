package dinhhung.mockmaster_server.repository;

import dinhhung.mockmaster_server.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByWorkspace_WorkspaceId(Long workspaceId);
}