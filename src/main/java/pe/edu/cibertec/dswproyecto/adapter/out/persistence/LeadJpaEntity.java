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
import pe.edu.cibertec.dswproyecto.domain.model.EstadoLead;

import java.time.LocalDateTime;

@Entity
@Table(name = "leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeadJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer clienteId;

    @Column
    private Integer propiedadId;

    @Column
    private Integer asesorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoLead estado;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(length = 500)
    private String observacion;

    @Column(nullable = false)
    private Boolean activo;
}
