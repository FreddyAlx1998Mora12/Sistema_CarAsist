package unl.academic.sistema_carasist.role.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rol {

    private Integer rol_Id;
    private String name;
}
