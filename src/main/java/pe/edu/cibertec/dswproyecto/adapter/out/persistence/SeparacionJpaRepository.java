package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoSeparacion;

import java.util.Collection;

public interface SeparacionJpaRepository extends JpaRepository<SeparacionJpaEntity, Integer> {

    boolean existsByPropiedadIdAndEstadoIn(Integer propiedadId, Collection<EstadoSeparacion> estados);
}
