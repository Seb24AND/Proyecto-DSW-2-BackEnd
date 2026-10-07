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
import pe.edu.cibertec.dswproyecto.domain.model.EstadoPropiedad;

import java.math.BigDecimal;

@Entity
@Table(name = "propiedades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropiedadJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 50)
    private String distrito;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal area;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPropiedad estado;

    @Column(nullable = false)
    private Boolean activo;
}
