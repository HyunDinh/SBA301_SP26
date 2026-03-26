package dinhhung.mockmaster_server.repository;

import dinhhung.mockmaster_server.entity.MockEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MockEndpointRepository extends JpaRepository<MockEndpoint, Long> {

    // Tìm tất cả endpoint thuộc về một folder cụ thể
    List<MockEndpoint> findByFolder_FolderId(Long folderId);

    // Kiểm tra xem một đường dẫn path đã tồn tại trong folder đó chưa (tránh trùng lặp API)
    boolean existsByPathAndMethodAndFolder_FolderId(String path, String method, Long folderId);
    long countByFolder_Workspace_WorkspaceId(Long workspaceId);

    @Query("SELECT e FROM MockEndpoint e " +
            "JOIN e.folder f " +
            "JOIN f.workspace w " +
            "JOIN w.user u " +
            "WHERE u.systemDomain = :domain " +
            "AND w.workspaceId = :wsId " +
            "AND e.path = :path " +
            "AND e.method = :method")
    java.util.Optional<MockEndpoint> findBySystemDomainAndWorkspaceAndPath(
            @Param("domain") String domain,
            @Param("wsId") Long wsId,
            @Param("path") String path,
            @Param("method") String method
    );


    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query("DELETE FROM MockEndpoint e WHERE e.folder.workspace.workspaceId = :workspaceId")
    void deleteAllByWorkspaceId(@Param("workspaceId") Long workspaceId);

}