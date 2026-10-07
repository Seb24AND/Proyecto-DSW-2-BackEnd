package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import pe.edu.cibertec.dswproyecto.domain.model.EstadoVenta;
import pe.edu.cibertec.dswproyecto.domain.model.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VentaResponse(
        Integer id,
        Integer separacionId,
        Integer leadId,
        Integer propiedadId,
        Integer asesorId,
        BigDecimal precioVenta,
        LocalDate fechaVenta,
        MetodoPago metodoPago,
        EstadoVenta estado,
        String observacion,
        Boolean activo
) {
}
