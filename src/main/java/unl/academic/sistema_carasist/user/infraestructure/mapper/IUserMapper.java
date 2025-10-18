package unl.academic.sistema_carasist.user.infraestructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserDTO;
import unl.academic.sistema_carasist.user.infraestructure.entity.UserEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUserMapper {

    User from_userEntity_toUser(UserEntity userEntity);
    UserEntity from_user_toUserEntity(User user);

    User from_userDTO_toUser(UserDTO userDTO);
    UserDTO fromUser_toUserDTO(User user);

    //Para que el usuario se registre
    //User registerRequestToUser(RegisterRequest registerRequest);

}
