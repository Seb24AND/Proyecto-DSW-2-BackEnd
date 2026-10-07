package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.Visita;
import pe.edu.cibertec.dswproyecto.domain.port.out.VisitaRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VisitaPersistenceAdapter implements VisitaRepositoryPort {

    private final VisitaJpaRepository visitaJpaRepository;

    @Override
    public List<Visita> listar() {
        return visitaJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Visita> buscarPorId(Integer id) {
        return visitaJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Visita guardar(Visita visita) {

        VisitaJpaEntity entity = toEntity(visita);

        VisitaJpaEntity guardada = visitaJpaRepository.save(entity);

        return toDomain(guardada);
    }

    @Override
    public void eliminar(Integer id) {
        visitaJpaRepository.deleteById(id);
    }

    private Visita toDomain(VisitaJpaEntity entity) {
        return new Visita(
                entity.getId(),
                entity.getLeadId(),
                entity.getFechaVisita(),
                entity.getEstado(),
                entity.getObservacion(),
                entity.getActivo()
        );
    }

    private VisitaJpaEntity toEntity(Visita visita) {
        return new VisitaJpaEntity(
                visita.getId(),
                visita.getLeadId(),
                visita.getFechaVisita(),
                visita.getEstado(),
                visita.getObservacion(),
                visita.getActivo()
        );
    }
}
