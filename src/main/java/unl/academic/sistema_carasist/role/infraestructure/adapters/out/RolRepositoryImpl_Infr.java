package unl.academic.sistema_carasist.role.infraestructure.adapters.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import unl.academic.sistema_carasist.role.application.ports.out.IRolRepository;
import unl.academic.sistema_carasist.role.domain.Rol;
import unl.academic.sistema_carasist.role.infraestructure.mappers.IRolesMapper;
import unl.academic.sistema_carasist.role.infraestructure.repository.IRolRepository_Infr;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RolRepositoryImpl_Infr implements IRolRepository {

    private final IRolRepository_Infr rolRepository;
    private final IRolesMapper rolesMapper;

    @Override
    public Rol save(Rol rol) {
        var rolEntity = rolesMapper.toRolEntity(rol);
        var rolSaved = rolRepository.save(rolEntity);
        return rolesMapper.toRol(rolSaved);
    }

    @Override
    public Rol update(Rol rol) {
        // Verificar que el rol existe
        var existingEntity = rolRepository.findById(rol.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol not found with id: " + rol.getRolId()));

        // Actualizar campos
        existingEntity.setName(rol.getName());

        // Guardar cambios
        var updatedEntity = rolRepository.save(existingEntity);
        return rolesMapper.toRol(updatedEntity);
    }

    @Override
    public Optional<Rol> findByName(String name) {
        return rolRepository.findByName(name)
                .map(rolesMapper::toRol);
    }

    @Override
    public List<Rol> findAll() {
        return rolRepository.findAll()
                .stream()
                .map(rolesMapper::toRol).collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return rolRepository.existsByName(name);
    }

}
