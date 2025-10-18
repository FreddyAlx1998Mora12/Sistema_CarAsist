package unl.academic.sistema_carasist.user.infraestructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import unl.academic.sistema_carasist.role.domain.Rol;
import unl.academic.sistema_carasist.role.infraestructure.entity.RolEntity;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String password;
    private String email;

    // Debe aplicarse un patron para identificar a una Persona, y relacionar
    // Relacionar con el rol, usuario con rol
    @OneToOne
    @JoinColumn(name = "rol_id")
    private RolEntity role;
}
