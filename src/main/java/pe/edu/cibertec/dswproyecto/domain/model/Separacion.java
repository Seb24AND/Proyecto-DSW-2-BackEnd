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
public class Separacion {

    private Integer id;
    private Integer leadId;
    private Integer propiedadId;
    private Integer asesorId;
    private BigDecimal montoSeparacion;
    private LocalDate fechaSeparacion;
    private LocalDate fechaVencimiento;
    private EstadoSeparacion estado;
    private String observacion;
    private Boolean activo;
}
