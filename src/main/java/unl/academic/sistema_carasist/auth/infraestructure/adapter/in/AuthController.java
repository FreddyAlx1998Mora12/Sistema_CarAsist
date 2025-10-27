package unl.academic.sistema_carasist.auth.infraestructure.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import unl.academic.sistema_carasist.auth.application.dtos.LoginRequestDTO;
import unl.academic.sistema_carasist.auth.application.dtos.LoginResponseDTO;
import unl.academic.sistema_carasist.auth.application.dtos.RefreshTokenRequest;
import unl.academic.sistema_carasist.auth.application.ports.in.IAuthenthicationUseCase;
import unl.academic.sistema_carasist.auth.application.ports.in.IRefreshTokenUseCase;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserRegisterDTO;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements IAuthController{

    private final IAuthenthicationUseCase authenticationUseCase;
    private final IRefreshTokenUseCase refreshTokenUseCase;



    @Override
    public ResponseEntity<LoginResponseDTO> register_user(UserRegisterDTO user) {
        return null;
    }

    /**
     * Endpoint para autenticación de usuarios
     * @param request Credenciales de login
     * @return Token de acceso y refresh token
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Login request received for user: {}", request.username());
        LoginResponseDTO response = authenticationUseCase.authenticate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para refrescar el token de acceso
     * @param request Refresh token
     * @return Nuevo token de acceso
     */
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Refresh token request received");
        LoginResponseDTO response = refreshTokenUseCase.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para cerrar sesión (revocar token)
     * @param authorization Bearer token en el header
     * @return Mensaje de confirmación
     */
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader("Authorization") String authorization
    ) {
        log.info("Logout request received");
        String token = authorization.substring(7); // Remover "Bearer "
        authenticationUseCase.logout(token);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    /**
     * Endpoint para validar el token actual
     * @param authentication Información del usuario autenticado
     * @return Información del usuario
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        log.info("Get current user request received");
        return ResponseEntity.ok(Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities()
        ));
    }

    /**
     * Endpoint de health check
     * @return Estado del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "auth-service"
        ));
    }
}
