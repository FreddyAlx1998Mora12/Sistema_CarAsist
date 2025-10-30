package unl.academic.sistema_carasist.user.infraestructure.mapper;

import org.mapstruct.*;
import unl.academic.sistema_carasist.role.infraestructure.mappers.IRolesMapper;
import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserDTO;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserRegisterDTO;
import unl.academic.sistema_carasist.user.infraestructure.entity.UserEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {IRolesMapper.class}
)
public interface IUserMapper {
    @Mapping(target = "roles", source = "roles")
    User from_userEntity_toUser(UserEntity userEntity);
    @Mapping(target = "roles", source = "roles")
    UserEntity from_user_toUserEntity(User user);
    @Mapping(target = "roles", source = "roles")
    User from_userDTO_toUser(UserDTO userDTO);
    @Mapping(target = "roles", source = "roles")
    UserDTO fromUser_toUserDTO(User user);


    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roles", ignore = true)  // Los roles se asignan en el service
    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "accountNonExpired", constant = "true")
    @Mapping(target = "accountNonLocked", constant = "true")
    @Mapping(target = "credentialsNonExpired", constant = "true")
    User fromRegisterDTO_toUser(UserRegisterDTO registerDTO);

    // Listas
    /**
     * Convierte lista de User (domain) a lista de UserDTO
     * Implementación manual porque MapStruct tiene problemas con Set<Rol>
     */
    default List<UserDTO> toListUserDTO(List<User> userList) {
        if (userList == null) {
            return null;
        }
        return userList.stream()
                .map(this::fromUser_toUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convierte lista de UserDTO a lista de User (domain)
     * Implementación manual porque MapStruct tiene problemas con Set<Rol>
     */
    default List<User> toListUser(List<UserDTO> userDTOList) {
        if (userDTOList == null) {
            return null;
        }
        return userDTOList.stream()
                .map(this::from_userDTO_toUser)
                .collect(Collectors.toList());
    }


    /*
    Podriamos utilizar para mandar a la base de datos como un string los roles
    */
    @Named("stringToSet")
    default Set<String> stringToSet(String roles) {
        if (roles == null || roles.isEmpty()) {
            return Set.of("USER");
        }
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    @Named("setToString")
    default String setToString(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return "USER";
        }
        return String.join(",", roles);
    }
}
