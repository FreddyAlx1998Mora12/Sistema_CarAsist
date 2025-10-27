package unl.academic.sistema_carasist.auth.application.dtos;

import java.time.LocalDateTime;

public record LoginResponseDTO(
        String token,
        String type,
        Integer userID,
        long expiresIn,
        String refreshToken,
        String username,
        LocalDateTime issuedAt
) {
}
