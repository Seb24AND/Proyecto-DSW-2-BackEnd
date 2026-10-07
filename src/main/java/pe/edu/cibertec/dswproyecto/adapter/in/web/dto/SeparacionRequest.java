package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoSeparacion;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SeparacionRequest {

    @NotNull
    private Integer leadId;

    @NotNull
    private Integer propiedadId;

    private Integer asesorId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal montoSeparacion;

    private LocalDate fechaSeparacion;

    private LocalDate fechaVencimiento;

    private EstadoSeparacion estado;

    @Size(max = 500)
    private String observacion;

    private Boolean activo;
}
