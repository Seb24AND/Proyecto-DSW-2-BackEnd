package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import pe.edu.cibertec.dswproyecto.domain.model.EstadoVisita;

import java.time.LocalDateTime;

public record VisitaResponse(
        Integer id,
        Integer leadId,
        LocalDateTime fechaVisita,
        EstadoVisita estado,
        String observacion,
        Boolean activo
) {
}
