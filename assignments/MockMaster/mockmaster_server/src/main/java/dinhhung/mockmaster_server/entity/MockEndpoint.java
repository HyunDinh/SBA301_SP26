package dinhhung.mockmaster_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MockEndpoint")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MockEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long endpointId;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false, length = 10)
    private String method; // GET, POST, PUT, DELETE...

    private Integer statusCode;

    @Lob
    @Column(columnDefinition = "VARCHAR(MAX)")
    private String responseBody;

    // THÊM: Để Frontend biết cách parse dữ liệu (application/json, text/html...)
    private String contentType;

    private Integer delayMs;

    @ManyToOne
    @JoinColumn(name = "FolderID")
    private Folder folder;
}