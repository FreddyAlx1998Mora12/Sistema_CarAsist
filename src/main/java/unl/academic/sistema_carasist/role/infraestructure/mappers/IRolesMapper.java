package unl.academic.sistema_carasist.role.infraestructure.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.ui.ModelMap;
import unl.academic.sistema_carasist.role.domain.Rol;
import unl.academic.sistema_carasist.role.infraestructure.dto.RolDTO;
import unl.academic.sistema_carasist.role.infraestructure.entity.RolEntity;
import unl.academic.sistema_carasist.user.domain.User;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserDTO;
import unl.academic.sistema_carasist.user.infraestructure.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IRolesMapper {

    Rol toRol(RolEntity userEntity);
    RolEntity toRolEntity(Rol user);

    @Mapping(source = "rolID", target = "rolId")
    @Mapping(source = "rolName", target = "name")
    Rol toRol_DTO(RolDTO userDTO);

    @Mapping(source = "rolId", target = "rolID")
    @Mapping(source = "name", target = "rolName")
    RolDTO toRolDTO(Rol user);

    List<Rol> toListRols(List<RolEntity> rolsEntities);
    List<RolDTO> toListRolDTOs(List<RolEntity> rolsEntities);

}
