package unl.academic.sistema_carasist.auth.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unl.academic.sistema_carasist.auth.domain.TokenType;
import unl.academic.sistema_carasist.auth.infraestructure.entity.TokenEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<TokenEntity, Integer> {

    Optional<TokenEntity> findByToken(String token);

    @Query("SELECT t FROM TokenEntity t WHERE t.userId = :userId AND t.revoked = false AND t.expired = false")
    List<TokenEntity> findAllValidTokensByUser(Integer userId);

    @Query("SELECT t FROM TokenEntity t WHERE t.token = :token AND t.revoked = false AND t.expired = false AND t.expiresAt > :now")
    Optional<TokenEntity> findValidToken(String token, LocalDateTime now);

    @Modifying
    @Query("UPDATE TokenEntity t SET t.revoked = true WHERE t.userId = :userId AND t.tokenType = :tokenType")
    void revokeAllUserTokensByType(Integer userId, TokenType tokenType);

    @Modifying
    @Query("DELETE FROM TokenEntity t WHERE t.expiresAt < :date")
    void deleteExpiredTokens(LocalDateTime date);
}
