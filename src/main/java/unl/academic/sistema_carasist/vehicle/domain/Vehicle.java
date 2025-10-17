package unl.academic.sistema_carasist.vehicle.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vehicle {

    private Integer id;
    private String placa;
    private String modelo;
    private String marca;
    private String color;
    private String cilindraje;
    private String tipo;
    private String claseTransporte;
}
