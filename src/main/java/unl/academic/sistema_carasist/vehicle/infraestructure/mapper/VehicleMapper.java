package unl.academic.sistema_carasist.vehicle.infraestructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import unl.academic.sistema_carasist.vehicle.domain.Vehicle;
import unl.academic.sistema_carasist.vehicle.infraestructure.entity.VehicleEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleMapper {

    VehicleEntity vehicleToVehicleEntity(Vehicle vehicle);
    Vehicle vehicleEntityToVehicle(VehicleEntity vehicleEntity);
}
