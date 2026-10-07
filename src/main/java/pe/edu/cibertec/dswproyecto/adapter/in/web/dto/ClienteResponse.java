package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import java.time.LocalDateTime;

public record ClienteResponse(
        Integer id,
        Integer usuarioId,
        LocalDateTime fechaRegistro,
        Boolean activo
) {
}
