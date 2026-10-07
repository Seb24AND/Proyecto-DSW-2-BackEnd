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
public class Visita {

    private Integer id;
    private Integer leadId;
    private LocalDateTime fechaVisita;
    private EstadoVisita estado;
    private String observacion;
    private Boolean activo;
}
