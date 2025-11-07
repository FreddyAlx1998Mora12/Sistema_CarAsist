package unl.academic.sistema_carasist.vehicle.infraestructure;

import org.springframework.http.ResponseEntity;
import unl.academic.sistema_carasist.vehicle.domain.Vehicle;

import java.util.List;

public interface VehicleController {

    ResponseEntity<Vehicle> save(Vehicle vehicle) throws Exception;
    ResponseEntity<Vehicle> update(Vehicle vehicle) throws Exception;
    ResponseEntity<List<Vehicle>> listAll() throws Exception;
    ResponseEntity<Vehicle> getById(Integer id) throws Exception;
    ResponseEntity<Vehicle> deletebyId(Integer id) throws Exception;
}
