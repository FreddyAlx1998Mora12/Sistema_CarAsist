package unl.academic.sistema_carasist.user.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unl.academic.sistema_carasist.user.infraestructure.entity.UserEntity;

import java.util.Optional;

@Repository
public interface IUserRepository_Inf extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
}
