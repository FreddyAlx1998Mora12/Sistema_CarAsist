package unl.academic.sistema_carasist.user.infraestructure.controller;

import org.springframework.http.ResponseEntity;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserDTO;

public interface IUserController {

    ResponseEntity<UserDTO> save(UserDTO user);
    ResponseEntity<UserDTO> update(UserDTO user);
    ResponseEntity<UserDTO> findById(Integer id);
    ResponseEntity<UserDTO> findByEmail(String email);
    ResponseEntity<UserDTO> findByUsername(String email);
    ResponseEntity<Void> deleteById(Integer id);

}
