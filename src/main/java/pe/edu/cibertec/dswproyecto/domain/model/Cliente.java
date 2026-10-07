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
public class Cliente {

    private Integer id;
    private Integer usuarioId;
    private LocalDateTime fechaRegistro;
    private Boolean activo;
}