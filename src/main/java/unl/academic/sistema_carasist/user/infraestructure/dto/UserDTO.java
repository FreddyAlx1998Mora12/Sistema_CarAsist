package unl.academic.sistema_carasist.user.infraestructure.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import unl.academic.sistema_carasist.role.infraestructure.dto.RolDTO;


import java.util.List;


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
    //@Builder.Default
    private List<RolDTO> roles;
}
