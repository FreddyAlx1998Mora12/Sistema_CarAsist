package unl.academic.sistema_carasist.vehicle.infraestructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unl.academic.sistema_carasist.vehicle.application.VehicleService;
import unl.academic.sistema_carasist.vehicle.domain.Vehicle;
import unl.academic.sistema_carasist.vehicle.infraestructure.VehicleController;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleControllerImpl implements VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Vehicle> save(@RequestBody Vehicle vehicle) throws Exception {
        return ResponseEntity.ok(vehicleService.save(vehicle));
    }

    @PutMapping
    public ResponseEntity<Vehicle> update(@RequestBody Vehicle vehicle) throws Exception {
        return ResponseEntity.ok(vehicleService.update(vehicle));
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> listAll() throws Exception {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> deletebyId(@PathVariable Integer id) throws Exception {
        return ResponseEntity.ok(vehicleService.delete(id));
    }
}
