package unl.academic.sistema_carasist.user.infraestructure.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unl.academic.sistema_carasist.user.application.ports.in.IUserService;
import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserDTO;
import unl.academic.sistema_carasist.user.infraestructure.mapper.IUserMapper;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserControllerImpl implements IUserController{

    private final IUserService userService;
    private final IUserMapper userMapper;

    @PostMapping
    @Override
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO user_dto) {
        User user = userMapper.from_userDTO_toUser(user_dto);
        User user_saved = userService.save(user);
        UserDTO userDTO = userMapper.fromUser_toUserDTO(user_saved);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping
    @Override
    public ResponseEntity<UserDTO> update(@RequestBody UserDTO user_dto) {
        User user = userMapper.from_userDTO_toUser(user_dto);
        User user_updated = userService.update(user);
        UserDTO userDTO = userMapper.fromUser_toUserDTO(user_updated);
        return ResponseEntity.ok(userDTO);
    }

    @Override
    public ResponseEntity<List<UserDTO>> findAll() {
        userService.findAll();
        //UserDTO userDTO = userMapper
        return null;
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        User user = userService.findById(id);
        UserDTO userDTO = userMapper.fromUser_toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{email}")
    @Override
    public ResponseEntity<UserDTO> findByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        UserDTO userDTO = userMapper.fromUser_toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> findByUsername(String username) {
        User user = userService.findByUsername(username);
        UserDTO userDTO = userMapper.fromUser_toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteById(Integer id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
