package unl.academic.sistema_carasist.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import unl.academic.sistema_carasist.exceptions.domain.NotFoundException;
import unl.academic.sistema_carasist.user.domain.IUserRepository;
import unl.academic.sistema_carasist.user.domain.IUserService;
import unl.academic.sistema_carasist.user.domain.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    // Inyecta, el tipico y clasico Autowired
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        Optional<User> user_registred = userRepository.findByUsername(user.getUsername());
        if (user_registred.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        if(user.getPassword() != null & !user.getPassword().trim().isBlank()) {
            if(passwordEncoder.matches(user.getPassword(), user_registred.get().getPassword())) {
                throw new IllegalArgumentException("Las contrasenias no deben ser ig");
            }
        }

        user_registred.get().setPassword(passwordEncoder.encode(user.getPassword()));
        user_registred.get().setUsername(user.getUsername());
        user_registred.get().setEmail(user.getEmail());

        return userRepository.update(user_registred.get());
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found by username"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found by email"));
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found by id"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
