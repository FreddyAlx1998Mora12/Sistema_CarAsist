package unl.academic.sistema_carasist.auth.infraestructure.adapter.in;

import org.springframework.http.ResponseEntity;
import unl.academic.sistema_carasist.auth.application.dtos.LoginRequestDTO;
import unl.academic.sistema_carasist.auth.application.dtos.LoginResponseDTO;
import unl.academic.sistema_carasist.auth.application.dtos.RefreshTokenRequest;
import unl.academic.sistema_carasist.user.infraestructure.dto.UserRegisterDTO;

import java.util.Map;

public interface IAuthController {

    ResponseEntity<LoginResponseDTO> register_user(UserRegisterDTO user);
    ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO);
    ResponseEntity<Map<String,String>> logout(String auth);
    ResponseEntity<LoginResponseDTO> refreshToken(RefreshTokenRequest auth_refresh_Token_Header);
}
