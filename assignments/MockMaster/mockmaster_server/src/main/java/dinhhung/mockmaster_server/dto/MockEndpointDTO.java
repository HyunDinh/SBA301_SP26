package dinhhung.mockmaster_server.dto;

import lombok.*;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class MockEndpointDTO {
    private Long endpointId;
    private String path;
    private String method;
    private Integer statusCode;
    private String responseBody;
    private String contentType;
    private Integer delayMs;
}