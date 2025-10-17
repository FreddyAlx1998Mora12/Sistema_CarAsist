package unl.academic.sistema_carasist.vehicle.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unl.academic.sistema_carasist.exceptions.domain.NotFoundException;
import unl.academic.sistema_carasist.vehicle.domain.Vehicle;
import unl.academic.sistema_carasist.vehicle.domain.VehicleRepository;
import unl.academic.sistema_carasist.vehicle.infraestructure.repository.implementation.PostgresVehicleRepositoryImpl;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
@Transactional
public class VehicleService {

    private final PostgresVehicleRepositoryImpl vehicleRepository;

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Vehicle vehicle) {
        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId())
                .orElseThrow(()-> new NotFoundException("El producto no fue encontrado"));
        return vehicleRepository.save(updatedVehicle);
    }

    public Vehicle findById(Integer id) {
        return vehicleRepository.findById(id).orElseThrow(()->new NotFoundException("El producto no fue encontrado"));
        //log.info("Found product in database with id {}", id);
    }

    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            log.info("No vehicles found");
        }else {
            log.info("Found {} vehicles", vehicles.size());
        }

        return vehicles;
    }

    public Vehicle delete(Integer id) {
        Vehicle vehicle = findById(id);
        vehicleRepository.deleteById(vehicle.getId());
        return vehicle;
    }
}
