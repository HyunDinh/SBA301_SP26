package dinhhung.mockmaster_server.controller;

import dinhhung.mockmaster_server.entity.MockEndpoint;
import dinhhung.mockmaster_server.service.MockEngineService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@RequestMapping("/mock")
@RequiredArgsConstructor
public class MockEngineController {

    private final MockEngineService mockEngineService;

    @RequestMapping("/{systemDomain}/{workspaceId}/**")
    public ResponseEntity<?> handleRequest(
            @PathVariable String systemDomain,
            @PathVariable Long workspaceId,
            HttpServletRequest request) {

        // 1. Kiểm tra tài khoản có bị khóa hay không trước khi làm bất cứ việc gì
        if (mockEngineService.isAccountLocked(systemDomain)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"error\": \"This API belongs to an account that has been locked.\"}");
        }

        // 2. Lấy phần Path dư thừa
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String subPath = new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);

        String finalPath = subPath.startsWith("/") ? subPath : "/" + subPath;
        String method = request.getMethod();

        // 3. Gọi Service để lấy dữ liệu Mock
        return mockEngineService.getMockResponse(systemDomain, workspaceId, finalPath, method)
                .map(ep -> ResponseEntity
                        .status(ep.getStatusCode())
                        .contentType(MediaType.valueOf(ep.getContentType()))
                        .body(ep.getResponseBody()))
                .orElse(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"Mock endpoint not found\"}"));
    }
}