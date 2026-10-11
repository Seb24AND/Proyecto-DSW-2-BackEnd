package pe.edu.cibertec.dswproyecto.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository usuarioJpaRepository;

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioJpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioJpaRepository.findByUsername(username)
                .map(this::toDomain);
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioJpaEntity entity = toEntity(usuario);
        UsuarioJpaEntity guardado = usuarioJpaRepository.save(entity);
        return toDomain(guardado);
    }

    @Override
    public boolean existePorUsername(String username) {
        return usuarioJpaRepository.existsByUsername(username);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }
    private Usuario toDomain(UsuarioJpaEntity entity) {
        return new Usuario(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getDni(),
                entity.getTelefono(),
                entity.getCorreo(),
                entity.getRol(),
                entity.getActivo()
        );
    }

    private UsuarioJpaEntity toEntity(Usuario usuario) {
        return new UsuarioJpaEntity(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDni(),
                usuario.getTelefono(),
                usuario.getCorreo(),
                usuario.getRol(),
                usuario.getActivo()
        );
    }
}
