package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import pe.edu.cibertec.dswproyecto.domain.model.EstadoPropiedad;

import java.math.BigDecimal;

public record PropiedadResponse(
        Integer id,
        String codigo,
        String tipo,
        String direccion,
        String distrito,
        BigDecimal area,
        BigDecimal precio,
        EstadoPropiedad estado,
        Boolean activo
) {
}
