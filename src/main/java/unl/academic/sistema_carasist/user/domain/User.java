package unl.academic.sistema_carasist.user.domain;

import lombok.Builder;
import lombok.Data;
import unl.academic.sistema_carasist.role.domain.Rol;

import java.util.List;

@Data
@Builder
public class User {

    private Integer userId;
    private String username;
    private String password;
    private String email;

    // Debe aplicarse un patron para identificar a una Persona, y relacionar
    // Necesarios para la seguridad
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    // Relacionar con el rol, usuario con rol
    //OneToMany
    private List<Rol> role;
}
