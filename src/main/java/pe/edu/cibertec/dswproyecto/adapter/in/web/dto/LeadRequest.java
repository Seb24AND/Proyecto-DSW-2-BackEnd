package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoLead;

@Getter
@Setter
@NoArgsConstructor
public class LeadRequest {

    @NotNull
    private Integer clienteId;

    private Integer propiedadId;

    private Integer asesorId;

    private EstadoLead estado;

    @Size(max = 500)
    private String observacion;

    private Boolean activo;
}
