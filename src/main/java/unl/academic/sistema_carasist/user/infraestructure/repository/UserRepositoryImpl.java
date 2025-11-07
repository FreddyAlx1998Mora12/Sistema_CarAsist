package unl.academic.sistema_carasist.user.infraestructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import unl.academic.sistema_carasist.user.application.ports.out.IUserRepository;
import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.entity.UserEntity;
import unl.academic.sistema_carasist.user.infraestructure.mapper.IUserMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements IUserRepository {

    private final IUserRepository_Inf userRepository;
    private final IUserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.from_user_toUserEntity(user);
        UserEntity userSaved = userRepository.save(userEntity);
        return userMapper.from_userEntity_toUser(userSaved);
    }

    @Override
    public User update(User user) {
        UserEntity userEntity = userMapper.from_user_toUserEntity(user);
        UserEntity userSaved = userRepository.save(userEntity);
        return userMapper.from_userEntity_toUser(userSaved);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id).map(userMapper::from_userEntity_toUser);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::from_userEntity_toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::from_userEntity_toUser);
    }

    @Override
    public Optional<User> findActiveUserByUsername(String username) {
        return userRepository.findActiveUserByUsername(username).map(userMapper::from_userEntity_toUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::from_userEntity_toUser).toList();
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
