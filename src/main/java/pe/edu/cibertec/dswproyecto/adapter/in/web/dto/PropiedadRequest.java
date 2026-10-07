package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoPropiedad;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PropiedadRequest {

    @NotBlank
    @Size(max = 20)
    private String codigo;

    @NotBlank
    @Size(max = 50)
    private String tipo;

    @NotBlank
    @Size(max = 150)
    private String direccion;

    @NotBlank
    @Size(max = 50)
    private String distrito;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal area;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal precio;

    @NotNull
    private EstadoPropiedad estado;

    private Boolean activo;
}
