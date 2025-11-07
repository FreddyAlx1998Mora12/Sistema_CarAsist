package unl.academic.sistema_carasist.user.infraestructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterDTO(
        @NotBlank(message = "Email requerido")
        @Email(message = "Debe ser un email valido")
        String email,

        @NotBlank(message = "El nombre de usuario no puede estar vacio")
        String username,

        @NotBlank(message = "la contrasenia no puede estar vacio")
        String password,

        @NotBlank(message = "El nombre no puede estar vacio")
        String name,

        @NotBlank(message = "Last name no pued estar vacio")
        String lastname
) {
}
