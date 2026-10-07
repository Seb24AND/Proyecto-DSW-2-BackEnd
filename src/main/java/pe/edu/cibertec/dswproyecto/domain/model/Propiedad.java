package pe.edu.cibertec.dswproyecto.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Propiedad {

    private Integer id;
    private String codigo;
    private String tipo;
    private String direccion;
    private String distrito;
    private BigDecimal area;
    private BigDecimal precio;
    private EstadoPropiedad estado;
    private Boolean activo;
}
