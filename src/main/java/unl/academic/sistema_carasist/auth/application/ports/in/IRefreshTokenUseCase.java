package unl.academic.sistema_carasist.auth.application.ports.in;

import unl.academic.sistema_carasist.auth.application.dtos.LoginResponseDTO;

public interface IRefreshTokenUseCase {
    LoginResponseDTO refreshToken(String refreshToken);
}
