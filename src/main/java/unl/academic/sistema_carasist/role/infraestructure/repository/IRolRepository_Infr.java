package unl.academic.sistema_carasist.role.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unl.academic.sistema_carasist.role.domain.Rol;
import unl.academic.sistema_carasist.role.infraestructure.entity.RolEntity;

import java.util.Optional;

@Repository
public interface IRolRepository_Infr extends JpaRepository<RolEntity, Integer> {

    Optional<RolEntity> findByName(String name);
    boolean existsByName(String name);
}
