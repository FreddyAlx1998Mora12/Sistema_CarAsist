package unl.academic.sistema_carasist.user.application.ports.out;

import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    User save(User user);
    User update(User user);
    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findActiveUserByUsername(String username);
    //Boolean existsByEmail(String email);
    List<User> findAll();
    void deleteById(Integer id);
}
