package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteRequest {

    @NotNull
    private Integer usuarioId;

    private Boolean activo;
}
