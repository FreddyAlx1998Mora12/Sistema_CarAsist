package unl.academic.sistema_carasist.user.domain;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    User save(User user);
    User update(User user);
    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    //Boolean existsByEmail(String email);
    List<User> findAll();
    void deleteById(Integer id);
}
