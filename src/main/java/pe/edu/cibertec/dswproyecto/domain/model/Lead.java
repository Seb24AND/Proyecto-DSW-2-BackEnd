package pe.edu.cibertec.dswproyecto.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lead {

    private Integer id;
    private Integer clienteId;
    private Integer propiedadId;
    private Integer asesorId;
    private EstadoLead estado;
    private LocalDateTime fechaRegistro;
    private String observacion;
    private Boolean activo;
}
