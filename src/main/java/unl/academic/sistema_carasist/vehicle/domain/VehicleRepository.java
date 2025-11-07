package unl.academic.sistema_carasist.vehicle.domain;

import java.util.List;
import java.util.Optional;

// Port
public interface VehicleRepository {

    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(Integer id);
    List<Vehicle> findAll();
    List<Vehicle> findByModel(String model);
    void deleteById(Integer id);
}
