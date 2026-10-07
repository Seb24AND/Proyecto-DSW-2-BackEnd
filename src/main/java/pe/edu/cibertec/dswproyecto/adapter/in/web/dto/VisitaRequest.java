package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoVisita;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class VisitaRequest {

    @NotNull
    private Integer leadId;

    @NotNull
    private LocalDateTime fechaVisita;

    private EstadoVisita estado;

    @Size(max = 500)
    private String observacion;

    private Boolean activo;
}
