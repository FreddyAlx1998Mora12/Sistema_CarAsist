package unl.academic.sistema_carasist.role.infraestructure.adapters.in;

import org.springframework.http.ResponseEntity;
import unl.academic.sistema_carasist.auth.application.dtos.LoginResponseDTO;
import unl.academic.sistema_carasist.role.infraestructure.dto.RolDTO;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserRegisterDTO;

import java.util.List;

public interface IRolController {
    ResponseEntity<RolDTO> save(RolDTO rol);
    ResponseEntity<RolDTO> update(RolDTO rol);
    ResponseEntity<RolDTO> findByName(String rol_name);

    ResponseEntity<List<RolDTO>> findAll();

}
