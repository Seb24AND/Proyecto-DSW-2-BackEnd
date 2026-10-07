package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.Cliente;
import pe.edu.cibertec.dswproyecto.domain.port.out.ClienteRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientePersistenceAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository clienteJpaRepository;

    @Override
    public List<Cliente> listar() {
        return clienteJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorUsuarioId(Integer usuarioId) {
        return clienteJpaRepository.findByUsuarioId(usuarioId)
                .map(this::toDomain);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteJpaEntity entity = toEntity(cliente);
        ClienteJpaEntity guardado = clienteJpaRepository.save(entity);
        return toDomain(guardado);
    }

    @Override
    public void eliminar(Integer id) {
        clienteJpaRepository.deleteById(id);
    }

    private Cliente toDomain(ClienteJpaEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getUsuarioId(),
                entity.getFechaRegistro(),
                entity.getActivo()
        );
    }

    private ClienteJpaEntity toEntity(Cliente cliente) {
        return new ClienteJpaEntity(
                cliente.getId(),
                cliente.getUsuarioId(),
                cliente.getFechaRegistro(),
                cliente.getActivo()
        );
    }
}
