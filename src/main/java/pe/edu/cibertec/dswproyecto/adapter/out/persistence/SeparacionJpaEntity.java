package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoSeparacion;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "separaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeparacionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer leadId;

    @Column(nullable = false)
    private Integer propiedadId;

    @Column
    private Integer asesorId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montoSeparacion;

    @Column(nullable = false)
    private LocalDate fechaSeparacion;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoSeparacion estado;

    @Column(length = 500)
    private String observacion;

    @Column(nullable = false)
    private Boolean activo;
}
