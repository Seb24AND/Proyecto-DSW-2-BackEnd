package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoSeparacion;
import pe.edu.cibertec.dswproyecto.domain.model.Separacion;
import pe.edu.cibertec.dswproyecto.domain.port.out.SeparacionRepositoryPort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeparacionPersistenceAdapter implements SeparacionRepositoryPort {

    private static final List<EstadoSeparacion> ESTADOS_ACTIVOS = Arrays.stream(EstadoSeparacion.values())
            .filter(EstadoSeparacion::esActiva)
            .toList();

    private final SeparacionJpaRepository separacionJpaRepository;

    @Override
    public List<Separacion> listar() {
        return separacionJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Separacion> buscarPorId(Integer id) {
        return separacionJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public boolean existeActivaPorPropiedadId(Integer propiedadId) {
        return separacionJpaRepository.existsByPropiedadIdAndEstadoIn(propiedadId, ESTADOS_ACTIVOS);
    }

    @Override
    public Separacion guardar(Separacion separacion) {

        SeparacionJpaEntity entity = toEntity(separacion);

        SeparacionJpaEntity guardado = separacionJpaRepository.save(entity);

        return toDomain(guardado);
    }

    @Override
    public void eliminar(Integer id) {
        separacionJpaRepository.deleteById(id);
    }

    private Separacion toDomain(SeparacionJpaEntity entity) {
        return new Separacion(
                entity.getId(),
                entity.getLeadId(),
                entity.getPropiedadId(),
                entity.getAsesorId(),
                entity.getMontoSeparacion(),
                entity.getFechaSeparacion(),
                entity.getFechaVencimiento(),
                entity.getEstado(),
                entity.getObservacion(),
                entity.getActivo()
        );
    }

    private SeparacionJpaEntity toEntity(Separacion separacion) {
        return new SeparacionJpaEntity(
                separacion.getId(),
                separacion.getLeadId(),
                separacion.getPropiedadId(),
                separacion.getAsesorId(),
                separacion.getMontoSeparacion(),
                separacion.getFechaSeparacion(),
                separacion.getFechaVencimiento(),
                separacion.getEstado(),
                separacion.getObservacion(),
                separacion.getActivo()
        );
    }
}
