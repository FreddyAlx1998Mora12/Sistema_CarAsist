package unl.academic.sistema_carasist.auth.infraestructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import unl.academic.sistema_carasist.auth.domain.Token;
import unl.academic.sistema_carasist.auth.infraestructure.entity.TokenEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ITokenMapper {
    @Mapping(source = "userId", target = "idUsuario")
    Token toToken(TokenEntity tokenEntity);
    @Mapping(source = "idUsuario", target = "userId")
    TokenEntity toTokenEntity(Token token);
}
