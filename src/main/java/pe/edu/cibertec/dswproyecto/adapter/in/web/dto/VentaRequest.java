package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoVenta;
import pe.edu.cibertec.dswproyecto.domain.model.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class VentaRequest {

    @NotNull
    private Integer separacionId;

    private Integer asesorId;

    @DecimalMin(value = "0.01")
    private BigDecimal precioVenta;

    private LocalDate fechaVenta;

    @NotNull
    private MetodoPago metodoPago;

    private EstadoVenta estado;

    @Size(max = 500)
    private String observacion;

    private Boolean activo;
}
