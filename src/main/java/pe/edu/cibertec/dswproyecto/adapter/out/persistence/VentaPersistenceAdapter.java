package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoVenta;
import pe.edu.cibertec.dswproyecto.domain.model.Venta;
import pe.edu.cibertec.dswproyecto.domain.port.out.VentaRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VentaPersistenceAdapter implements VentaRepositoryPort {

    private final VentaJpaRepository ventaJpaRepository;

    @Override
    public List<Venta> listar() {
        return ventaJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Venta> buscarPorId(Integer id) {
        return ventaJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public boolean existeActivaPorSeparacionId(Integer separacionId) {
        return ventaJpaRepository.existsBySeparacionIdAndEstadoNot(separacionId, EstadoVenta.CANCELADA);
    }

    @Override
    public boolean existePorSeparacionId(Integer separacionId) {
        return ventaJpaRepository.existsBySeparacionId(separacionId);
    }

    @Override
    public Venta guardar(Venta venta) {

        VentaJpaEntity entity = toEntity(venta);

        VentaJpaEntity guardado = ventaJpaRepository.save(entity);

        return toDomain(guardado);
    }

    @Override
    public void eliminar(Integer id) {
        ventaJpaRepository.deleteById(id);
    }

    private Venta toDomain(VentaJpaEntity entity) {
        return new Venta(
                entity.getId(),
                entity.getSeparacionId(),
                entity.getLeadId(),
                entity.getPropiedadId(),
                entity.getAsesorId(),
                entity.getPrecioVenta(),
                entity.getFechaVenta(),
                entity.getMetodoPago(),
                entity.getEstado(),
                entity.getObservacion(),
                entity.getActivo()
        );
    }

    private VentaJpaEntity toEntity(Venta venta) {
        return new VentaJpaEntity(
                venta.getId(),
                venta.getSeparacionId(),
                venta.getLeadId(),
                venta.getPropiedadId(),
                venta.getAsesorId(),
                venta.getPrecioVenta(),
                venta.getFechaVenta(),
                venta.getMetodoPago(),
                venta.getEstado(),
                venta.getObservacion(),
                venta.getActivo()
        );
    }
}
