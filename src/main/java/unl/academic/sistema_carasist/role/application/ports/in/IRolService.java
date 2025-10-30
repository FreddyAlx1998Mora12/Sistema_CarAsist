package unl.academic.sistema_carasist.role.application.ports.in;

import unl.academic.sistema_carasist.role.domain.Rol;
import unl.academic.sistema_carasist.role.infraestructure.dto.RolDTO;

import java.util.List;
import java.util.Optional;

public interface IRolService {

    RolDTO save(RolDTO rolDTO);
    RolDTO update(RolDTO rolDTO);
    RolDTO findByName(String name);
    List<RolDTO> findAll();
    boolean existsByName(String name);

}
