package unl.academic.sistema_carasist.user.domain;

import lombok.Builder;
import lombok.Data;
import unl.academic.sistema_carasist.role.domain.Rol;

@Data
@Builder
public class User {

    private Integer userId;
    private String username;
    private String password;
    private String email;

    // Debe aplicarse un patron para identificar a una Persona, y relacionar

    // Relacionar con el rol, usuario con rol
    private Rol role;
}
