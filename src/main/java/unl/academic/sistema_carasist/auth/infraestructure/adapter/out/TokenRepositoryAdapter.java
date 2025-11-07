package unl.academic.sistema_carasist.auth.infraestructure.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import unl.academic.sistema_carasist.auth.application.ports.out.ITokenRepositoryPort;
import unl.academic.sistema_carasist.auth.domain.Token;
import unl.academic.sistema_carasist.auth.domain.TokenType;
import unl.academic.sistema_carasist.auth.infraestructure.mapper.ITokenMapper;
import unl.academic.sistema_carasist.auth.infraestructure.repository.ITokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements ITokenRepositoryPort {

    private final ITokenRepository token_repository;
    private final ITokenMapper token_mapper;

    @Override
    public Token save(Token token) {
        var token_entity = token_mapper.toTokenEntity(token);
        var token_saved = token_repository.save(token_entity);
        return token_mapper.toToken(token_saved);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return token_repository.findByToken(token)
                .map(token_mapper::toToken);
    }

    @Override
    public Optional<Token> findValidToken(String token) {
        return token_repository.findValidToken(token, LocalDateTime.now())
                .map(token_mapper::toToken);
    }


    @Override
    @Transactional
    public void revokeAllUserTokensByType(Integer userId, TokenType tokenType) {
        token_repository.revokeAllUserTokensByType(userId, tokenType);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        token_repository.deleteExpiredTokens(LocalDateTime.now());
    }
}
