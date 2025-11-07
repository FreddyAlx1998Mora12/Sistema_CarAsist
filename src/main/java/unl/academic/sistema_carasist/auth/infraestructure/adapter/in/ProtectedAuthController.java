package unl.academic.sistema_carasist.auth.infraestructure.adapter.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/protected")
@RequiredArgsConstructor
public class ProtectedAuthController {

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, String>> userEndpoint() {
        log.info("User endpoint accessed");
        return ResponseEntity.ok(Map.of(
                "message", "This is a USER protected resource",
                "access", "USER or ADMIN role required"
        ));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> adminEndpoint() {
        log.info("Admin endpoint accessed");
        return ResponseEntity.ok(Map.of(
                "message", "This is an ADMIN protected resource",
                "access", "ADMIN role required"
        ));
    }
}
