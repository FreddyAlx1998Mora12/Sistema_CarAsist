package unl.academic.sistema_carasist.user.application.ports.in;

import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserRegisterDTO;

import java.util.List;

public interface IUserService {

    User save(User user);
    User update(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    User findById(Integer id);
    List<User> findAll();
    void deleteById(Integer id);

    User register(UserRegisterDTO registerDTO);
}
