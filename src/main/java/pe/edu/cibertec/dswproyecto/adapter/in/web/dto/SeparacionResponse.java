package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import pe.edu.cibertec.dswproyecto.domain.model.EstadoSeparacion;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SeparacionResponse(
        Integer id,
        Integer leadId,
        Integer propiedadId,
        Integer asesorId,
        BigDecimal montoSeparacion,
        LocalDate fechaSeparacion,
        LocalDate fechaVencimiento,
        EstadoSeparacion estado,
        String observacion,
        Boolean activo
) {
}
