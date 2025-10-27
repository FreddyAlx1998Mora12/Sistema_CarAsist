package unl.academic.sistema_carasist.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class Token {

    private String token;
    private String refreshToken;
    private String expiresIn;
    private Integer idUsuario;
    private TokenType tokenType;

    private boolean revoked;
    private boolean expired;

    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    // Metodo que deberia incluir en Servicio o en algo por el estilo
    public boolean isValid() {
        return !revoked && !expired && expiresAt.isAfter(LocalDateTime.now());
    }
}
