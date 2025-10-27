package unl.academic.sistema_carasist.auth.infraestructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import unl.academic.sistema_carasist.auth.domain.Token;
import unl.academic.sistema_carasist.auth.infraestructure.entity.TokenEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ITokenMapper {

    Token toToken(TokenEntity tokenEntity);
    TokenEntity toTokenEntity(Token token);
}
