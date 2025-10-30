package unl.academic.sistema_carasist.role.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import unl.academic.sistema_carasist.role.application.ports.in.IRolService;
import unl.academic.sistema_carasist.role.application.ports.out.IRolRepository;
import unl.academic.sistema_carasist.role.domain.Rol;
import unl.academic.sistema_carasist.role.infraestructure.dto.RolDTO;
import unl.academic.sistema_carasist.role.infraestructure.mappers.IRolesMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RolServiceImpl implements IRolService {

    private final IRolRepository rolRepository;
    private final IRolesMapper rolesMapper;


    @Override
    public RolDTO save(RolDTO rolDTO) {
        log.info("Saving new role: {}", rolDTO.rolName());

        // Validar que el nombre no exista
        if (rolRepository.existsByName(rolDTO.rolName())) {
            throw new IllegalArgumentException("Role with name '" + rolDTO.rolName() + "' already exists");
        }

        // Convertir DTO a dominio
        Rol rol = rolesMapper.toRol(rolDTO);

        // Guardar
        Rol savedRol = rolRepository.save(rol);

        log.info("Role saved successfully with id: {}", savedRol.getRolId());
        return rolesMapper.toRolDTO(savedRol);
    }

    @Override
    public RolDTO update(RolDTO rolDTO) {
        log.info("Updating role with id: {}", rolDTO.rolID());

        // Validar que el rol existe
        if (rolDTO.rolID() == null) {
            throw new IllegalArgumentException("Role ID is required for update");
        }

        // Verificar si el nuevo nombre ya existe en otro rol
        rolRepository.findByName(rolDTO.rolName()).ifPresent(existingRol -> {
            if (!existingRol.getRolId().equals(rolDTO.rolID())) {
                throw new IllegalArgumentException("Role name already exists");
            }
        });

        // Convertir y actualizar
        Rol rol = rolesMapper.toRol(rolDTO);
        Rol updatedRol = rolRepository.update(rol);

        log.info("Role updated successfully: {}", updatedRol.getName());
        return rolesMapper.toRolDTO(updatedRol);
    }

    @Override
    public RolDTO findByName(String name) {
        log.debug("Finding role by name: {}", name);

        return rolRepository.findByName(name)
                .map(rolesMapper::toRolDTO)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + name));
    }

    @Override
    public List<RolDTO> findAll() {
        log.debug("Finding all roles");

        return rolRepository.findAll()
                .stream()
                .map(rolesMapper::toRolDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return rolRepository.existsByName(name);
    }
}
