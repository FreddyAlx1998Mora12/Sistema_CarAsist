package unl.academic.sistema_carasist.vehicle.infraestructure.repository.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import unl.academic.sistema_carasist.vehicle.domain.Vehicle;
import unl.academic.sistema_carasist.vehicle.domain.VehicleRepository;
import unl.academic.sistema_carasist.vehicle.infraestructure.entity.VehicleEntity;
import unl.academic.sistema_carasist.vehicle.infraestructure.mapper.VehicleMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostgresVehicleRepositoryImpl implements VehicleRepository {

    private final QueryVehicleRepositoryJPA queryVehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public Vehicle save(Vehicle vehicle) {
        VehicleEntity vehicle_saved = queryVehicleRepository.save(vehicleMapper.vehicleToVehicleEntity(vehicle));
        return vehicleMapper.vehicleEntityToVehicle(vehicle_saved);
    }

    @Override
    public Optional<Vehicle> findById(Integer id) {
        // Validar si no lo encuentra por ID
        return queryVehicleRepository.findById(id).map(vehicleMapper::vehicleEntityToVehicle);
    }

    @Override
    public List<Vehicle> findAll() {
        return queryVehicleRepository.findAll()
                .stream()
                .map(vehicleMapper::vehicleEntityToVehicle)
                .toList();
    }

    @Override
    public List<Vehicle> findByModel(String model) {
        // implementar logica
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {
        queryVehicleRepository.deleteById(id);
    }
}
