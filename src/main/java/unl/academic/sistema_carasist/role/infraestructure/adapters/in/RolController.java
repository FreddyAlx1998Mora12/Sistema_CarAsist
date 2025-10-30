package unl.academic.sistema_carasist.role.infraestructure.adapters.in;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unl.academic.sistema_carasist.exceptions.domain.NotFoundException;
import unl.academic.sistema_carasist.role.application.ports.in.IRolService;
import unl.academic.sistema_carasist.role.infraestructure.dto.RolDTO;
import unl.academic.sistema_carasist.role.infraestructure.mappers.IRolesMapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@RestController
public class RolController implements IRolController{

    private final IRolService rolService;
    private final IRolesMapper rolMapper;

    //@Operation(summary = "Crear nuevo rol", description = "Solo accesible por ADMIN")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<RolDTO> save(@Valid @RequestBody RolDTO rol) {
        log.info("POST /api/v1/roles - Create role: {}", rol.rolName());
        RolDTO savedRol = rolService.save(rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRol);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<RolDTO> update(@Valid @RequestBody RolDTO rol) {
        log.info("PUT /api/v1/roles - Update role: {}", rol.rolName());
        RolDTO updatedRol = rolService.update(rol);
        return ResponseEntity.ok(updatedRol);
    }

    @GetMapping("/name/{rolName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<RolDTO> findByName(@PathVariable String rolName) {
        log.info("GET /api/v1/roles/name/{}", rolName);
        RolDTO rol = rolService.findByName(rolName);
        return ResponseEntity.ok(rol);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<List<RolDTO>> findAll() {
        log.info("GET /api/v1/roles - Get all roles");
        List<RolDTO> roles = rolService.findAll();
        return ResponseEntity.ok(roles);
    }
}
