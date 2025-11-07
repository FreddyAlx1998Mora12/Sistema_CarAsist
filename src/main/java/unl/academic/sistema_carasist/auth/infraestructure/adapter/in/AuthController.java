package unl.academic.sistema_carasist.auth.infraestructure.adapter.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import unl.academic.sistema_carasist.auth.application.dtos.LoginRequestDTO;
import unl.academic.sistema_carasist.auth.application.dtos.LoginResponseDTO;
import unl.academic.sistema_carasist.auth.application.dtos.RefreshTokenRequest;
import unl.academic.sistema_carasist.auth.application.ports.in.IAuthenthicationUseCase;
import unl.academic.sistema_carasist.auth.application.ports.in.IRefreshTokenUseCase;
import unl.academic.sistema_carasist.role.application.ports.out.IRolRepository;
import unl.academic.sistema_carasist.role.domain.Rol;
import unl.academic.sistema_carasist.user.application.ports.in.IUserService;
import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserDTO;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserRegisterDTO;
import unl.academic.sistema_carasist.user.infraestructure.mapper.IUserMapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController implements IAuthController{

    private final IAuthenthicationUseCase authenticationUseCase;
    private final IRefreshTokenUseCase refreshTokenUseCase;
    private final PasswordEncoder passwordEncoder;

    private final IUserService userService;
    private final IUserMapper userMapper;
    private final IRolRepository rolRepository;

    /**
     * Endpoint para el registro de usuarios
     * @param userRegisterDTO Datos de Usuario
     * @return Token de acceso y refresh token,a la vez, cuenta creada
     */
    @PostMapping("/register")
    @Override
    public ResponseEntity<LoginResponseDTO> register_user(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("User registration request received for username: {}", userRegisterDTO.username());

        try {
            // ================================================================
            // PASO 1: Validar que el username no exista
            // ================================================================
            try {
                userService.findByUsername(userRegisterDTO.username());
                // Si llega aquí, el usuario existe
                log.warn("Registration failed: Username already exists: {}", userRegisterDTO.username());
                throw new IllegalArgumentException("Username '" + userRegisterDTO.username() + "' already exists");
            } catch (RuntimeException e) {
                // Usuario no existe, continuar (esto es lo esperado)
                if (e.getMessage().contains("already exists")) {
                    throw e;  // Re-lanzar si es error de duplicado
                }
                // Si es NotFoundException, continuar
            }

            // ================================================================
            // PASO 2: Validar que el email no exista
            // ================================================================
            try {
                userService.findByEmail(userRegisterDTO.email());
                // Si llega aquí, el email existe
                log.warn("Registration failed: Email already exists: {}", userRegisterDTO.email());
                throw new IllegalArgumentException("Email '" + userRegisterDTO.email() + "' is already registered");
            } catch (RuntimeException e) {
                // Email no existe, continuar
                if (e.getMessage().contains("already registered")) {
                    throw e;
                }
            }

            // ================================================================
            // PASO 3: Obtener rol USER_EX (Dueño de Vehículo) por defecto
            // ================================================================
            Rol defaultRole = rolRepository.findByName("USER_EX")
                    .orElseThrow(() -> {
                        log.error("Critical: Default role USER_EX not found in database");
                        return new IllegalStateException(
                                "System configuration error: Default role USER_EX not found. Please contact administrator."
                        );
                    });

            log.debug("Default role USER_EX found with id: {}", defaultRole.getRolId());

            // ================================================================
            // PASO 4: Crear usuario con datos del DTO
            // ================================================================
            User newUser = User.builder()
                    .username(userRegisterDTO.username())
                    .email(userRegisterDTO.email())
                    .password(passwordEncoder.encode(userRegisterDTO.password()))  // Encriptar password
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build();

            // ================================================================
            // PASO 5: Asignar rol USER_EX al usuario
            // ================================================================
            Set<Rol> roles = new HashSet<>();
            roles.add(defaultRole);
            newUser.setRoles(roles);

            log.debug("User object created with role USER_EX: {}", newUser.getUsername());

            // ================================================================
            // PASO 6: Guardar usuario en base de datos
            // ================================================================
            User savedUser = userService.save(newUser);
            log.info("User registered successfully: {} with id: {}",
                    savedUser.getUsername(), savedUser.getUserId());

            // ================================================================
            // PASO 7: Autenticar automáticamente al usuario (generar tokens)
            // ================================================================
            LoginRequestDTO loginRequest = new LoginRequestDTO(
                    userRegisterDTO.username(),
                    userRegisterDTO.password(),
                    userRegisterDTO.email()
            );

            LoginResponseDTO response = authenticationUseCase.authenticate(loginRequest);

            log.info("User registered and authenticated successfully: {}", savedUser.getUsername());

            // ================================================================
            // PASO 8: Retornar respuesta con tokens
            // ================================================================
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.error("Registration validation error: {}", e.getMessage());
            throw e;
        } catch (IllegalStateException e) {
            log.error("System configuration error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during user registration: {}", e.getMessage(), e);
            throw new RuntimeException("Error registering user. Please try again later.");
        }
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
