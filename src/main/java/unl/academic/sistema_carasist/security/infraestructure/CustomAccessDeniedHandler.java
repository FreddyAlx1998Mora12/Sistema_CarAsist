package unl.academic.sistema_carasist.security.infraestructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import unl.academic.sistema_carasist.exceptions.domain.ErrorResponse;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Manejador personalizado para errores de autorización (403 Forbidden).
 * Se activa cuando:
 * - El usuario ESTÁ autenticado
 * - PERO no tiene los permisos/roles necesarios para acceder al recurso
 * - Ejemplo: Usuario con rol USER intenta acceder a endpoint que requiere ADMIN
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler() {
        this.objectMapper = new ObjectMapper();
        // Registrar módulo para serializar LocalDateTime
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("Access denied: {} - Path: {} - User: {}",
                accessDeniedException.getMessage(),
                request.getRequestURI(),
                request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "Anonymous");

        // Crear respuesta de error
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .message("Access denied: You don't have permission to access this resource")
                .path(request.getRequestURI())
                .build();

        // Configurar respuesta HTTP
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // Escribir JSON en la respuesta
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
