package unl.academic.sistema_carasist.auth.application.ports.in;

public interface IValidateTokenUseCase {
    boolean validateToken(String token);
    void revokeToken(String token);
}
