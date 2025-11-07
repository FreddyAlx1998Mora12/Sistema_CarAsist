package unl.academic.sistema_carasist.auth.application.ports.out;

import unl.academic.sistema_carasist.auth.domain.Token;
import unl.academic.sistema_carasist.auth.domain.TokenType;

import java.util.Optional;

public interface ITokenRepositoryPort {
    Token save(Token token);
    Optional<Token> findByToken(String token);
    Optional<Token> findValidToken(String token);
    void revokeAllUserTokensByType(Integer userId, TokenType tokenType);
    void deleteExpiredTokens();
}
