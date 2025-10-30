package unl.academic.sistema_carasist.role.application.ports.out;

import unl.academic.sistema_carasist.role.domain.Rol;

import java.util.List;
import java.util.Optional;

public interface IRolRepository {

    Rol save(Rol rol);
    Rol update(Rol rol);
    //Rol findByName(String name);
    Optional<Rol> findByName(String name);
    //Optional<Rol> findById(Long id);
    List<Rol> findAll();
    boolean existsByName(String name);
}
