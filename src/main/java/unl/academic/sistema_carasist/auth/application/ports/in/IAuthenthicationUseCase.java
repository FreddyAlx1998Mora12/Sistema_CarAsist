package unl.academic.sistema_carasist.auth.application.ports.in;

import unl.academic.sistema_carasist.auth.application.dtos.LoginRequestDTO;
import unl.academic.sistema_carasist.auth.application.dtos.LoginResponseDTO;

public interface IAuthenthicationUseCase {
    LoginResponseDTO authenticate(LoginRequestDTO request);
    void logout(String token);
}
