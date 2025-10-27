package unl.academic.sistema_carasist.security.infraestructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import unl.academic.sistema_carasist.exceptions.domain.ErrorResponse;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Manejador personalizado para errores de autenticación (401 Unauthorized).
 * Se activa cuando:
 * - No hay token en la petición
 * - El token es inválido
 * - El token está expirado o revocado
 * - Cualquier fallo de autenticación
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Aqui no necesito el @requiredArgs
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        // Registrar módulo para serializar LocalDateTime
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {} - Path: {}",
                authException.getMessage(),
                request.getRequestURI());

        // Crear respuesta de error
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message("Authentication failed: " + authException.getMessage())
                .path(request.getRequestURI())
                .build();

        // Configurar respuesta HTTP
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // Escribir JSON en la respuesta
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
