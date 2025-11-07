package unl.academic.sistema_carasist.user.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import unl.academic.sistema_carasist.user.infraestructure.entity.UserEntity;

import java.util.Optional;

@Repository
public interface IUserRepository_Inf extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.username = :username AND u.enabled = true")
    Optional<UserEntity> findActiveUserByUsername(String username);
}
