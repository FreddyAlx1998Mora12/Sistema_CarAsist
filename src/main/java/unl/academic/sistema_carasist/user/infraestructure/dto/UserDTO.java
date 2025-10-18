package unl.academic.sistema_carasist.user.infraestructure.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unl.academic.sistema_carasist.role.domain.Rol;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Integer userId;
    private String username;
    private String password;
    private String email;

    // Debe aplicarse un patron para identificar a una Persona, y relacionar
    private Rol role;
}
