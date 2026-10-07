package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.Propiedad;
import pe.edu.cibertec.dswproyecto.domain.port.out.PropiedadRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PropiedadPersistenceAdapter implements PropiedadRepositoryPort {

    private final PropiedadJpaRepository propiedadJpaRepository;

    @Override
    public List<Propiedad> listar() {
        return propiedadJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Propiedad> buscarPorId(Integer id) {
        return propiedadJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Propiedad guardar(Propiedad propiedad) {

        PropiedadJpaEntity entity = toEntity(propiedad);

        PropiedadJpaEntity guardado = propiedadJpaRepository.save(entity);

        return toDomain(guardado);
    }

    @Override
    public void eliminar(Integer id) {
        propiedadJpaRepository.deleteById(id);
    }

    private Propiedad toDomain(PropiedadJpaEntity entity) {
        return new Propiedad(
                entity.getId(),
                entity.getCodigo(),
                entity.getTipo(),
                entity.getDireccion(),
                entity.getDistrito(),
                entity.getArea(),
                entity.getPrecio(),
                entity.getEstado(),
                entity.getActivo()
        );
    }

    private PropiedadJpaEntity toEntity(Propiedad propiedad) {
        return new PropiedadJpaEntity(
                propiedad.getId(),
                propiedad.getCodigo(),
                propiedad.getTipo(),
                propiedad.getDireccion(),
                propiedad.getDistrito(),
                propiedad.getArea(),
                propiedad.getPrecio(),
                propiedad.getEstado(),
                propiedad.getActivo()
        );
    }
}