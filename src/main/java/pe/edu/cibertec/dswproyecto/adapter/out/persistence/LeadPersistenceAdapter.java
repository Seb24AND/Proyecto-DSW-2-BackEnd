package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.Lead;
import pe.edu.cibertec.dswproyecto.domain.port.out.LeadRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LeadPersistenceAdapter implements LeadRepositoryPort {

    private final LeadJpaRepository leadJpaRepository;

    @Override
    public List<Lead> listar() {
        return leadJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Lead> buscarPorId(Integer id) {
        return leadJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Lead guardar(Lead lead) {

        LeadJpaEntity entity = toEntity(lead);

        LeadJpaEntity guardado = leadJpaRepository.save(entity);

        return toDomain(guardado);
    }

    @Override
    public void eliminar(Integer id) {
        leadJpaRepository.deleteById(id);
    }

    private Lead toDomain(LeadJpaEntity entity) {
        return new Lead(
                entity.getId(),
                entity.getClienteId(),
                entity.getPropiedadId(),
                entity.getAsesorId(),
                entity.getEstado(),
                entity.getFechaRegistro(),
                entity.getObservacion(),
                entity.getActivo()
        );
    }

    private LeadJpaEntity toEntity(Lead lead) {
        return new LeadJpaEntity(
                lead.getId(),
                lead.getClienteId(),
                lead.getPropiedadId(),
                lead.getAsesorId(),
                lead.getEstado(),
                lead.getFechaRegistro(),
                lead.getObservacion(),
                lead.getActivo()
        );
    }
}
