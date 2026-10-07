package pe.edu.cibertec.dswproyecto.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    private Integer id;
    private Integer separacionId;
    private Integer leadId;
    private Integer propiedadId;
    private Integer asesorId;
    private BigDecimal precioVenta;
    private LocalDate fechaVenta;
    private MetodoPago metodoPago;
    private EstadoVenta estado;
    private String observacion;
    private Boolean activo;
}
