package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoVenta;

public interface VentaJpaRepository extends JpaRepository<VentaJpaEntity, Integer> {

    boolean existsBySeparacionIdAndEstadoNot(Integer separacionId, EstadoVenta estado);

    boolean existsBySeparacionId(Integer separacionId);
}
