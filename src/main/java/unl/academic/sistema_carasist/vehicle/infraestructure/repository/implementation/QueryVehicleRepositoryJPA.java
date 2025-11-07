package unl.academic.sistema_carasist.vehicle.infraestructure.repository.implementation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unl.academic.sistema_carasist.vehicle.infraestructure.entity.VehicleEntity;


@Repository
public interface QueryVehicleRepositoryJPA extends JpaRepository<VehicleEntity, Integer> {
}
