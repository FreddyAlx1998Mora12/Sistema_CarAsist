package unl.academic.sistema_carasist.user.application.ports.in;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unl.academic.sistema_carasist.exceptions.domain.NotFoundException;
import unl.academic.sistema_carasist.role.application.ports.out.IRolRepository;
import unl.academic.sistema_carasist.role.domain.Rol;
import unl.academic.sistema_carasist.user.application.ports.out.IUserRepository;
import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserRegisterDTO;
import unl.academic.sistema_carasist.user.infraestructure.mapper.IUserMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    // Inyecta, el tipico y clasico Autowired
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IUserMapper userMapper;
    private final IRolRepository rolRepository;

    @Override
    public User save(User user) {
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) { // Simple check si ya está codificada
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername()) // Cambiado a findByUsername
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Actualizar solo los campos que vienen en 'user' y que son modificables
        existingUser.setEmail(user.getEmail());
        // Añadir nombre y apellido si los tienes en User
        existingUser.setUsername(user.getUsername());


        // Lógica para la contraseña actualizada
        if (user.getPassword() != null && !user.getPassword().trim().isBlank()) {
            // Verificar si la nueva contraseña es diferente a la existente (codificada)
            // No es buena práctica comparar contraseñas codificadas directamente si el usuario envía texto plano
            // Lo más común es: si se proporciona una nueva contraseña, codificarla y guardarla.
            // Si quieres evitar que ponga la "misma" contraseña (en texto plano), tendrías que decodificarla, lo cual es imposible/malo.
            // O comparar el hash si la nueva contraseña ya viene hasheada (que no es el caso de un formulario).
            // Si el user envía la contraseña sin codificar, la codificamos y guardamos.
            if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                // Si la contraseña nueva es la misma que la anterior (después de codificar),
                // podrías lanzar una excepción o simplemente no hacer nada.
                // Tu lógica actual de "Las contrasenias no deben ser ig" es aquí.
                throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la anterior.");
            }
        }
        // También puedes actualizar los roles si el 'user' de entrada los tiene.
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existingUser.setRoles(user.getRoles());
        }

        return userRepository.update(existingUser);
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

    @Override
    @Transactional
    public User register(UserRegisterDTO registerDTO) {
        // 1. Validar que el username no exista
        userRepository.findByUsername(registerDTO.username())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Username already exists");
                });

        // 2. Validar que el email no exista
        userRepository.findByEmail(registerDTO.email())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Email already exists");
                });

        // 3. Mapear DTO a User
        User user = userMapper.fromRegisterDTO_toUser(registerDTO);

        // 4. Encriptar contraseña
        user.setPassword(passwordEncoder.encode(registerDTO.password()));

        // 5. Asignar rol por defecto "USER_EX" (Dueño de vehículo)
        Rol defaultRol = rolRepository.findByName("USER_EX")
                .orElseThrow(() -> new NotFoundException("Default role USER_EX not found"));

        Set<Rol> roles = new HashSet<>();
        roles.add(defaultRol);
        user.setRoles(roles);

        return userRepository.save(user);
    }
}
