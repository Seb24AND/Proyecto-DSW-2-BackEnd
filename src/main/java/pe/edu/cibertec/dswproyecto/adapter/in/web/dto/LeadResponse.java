package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import pe.edu.cibertec.dswproyecto.domain.model.EstadoLead;

import java.time.LocalDateTime;

public record LeadResponse(
        Integer id,
        Integer clienteId,
        Integer propiedadId,
        Integer asesorId,
        EstadoLead estado,
        LocalDateTime fechaRegistro,
        String observacion,
        Boolean activo
) {
}
