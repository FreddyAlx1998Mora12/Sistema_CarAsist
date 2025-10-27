package unl.academic.sistema_carasist.security.application.ports;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
    String generateAccessToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);
}
