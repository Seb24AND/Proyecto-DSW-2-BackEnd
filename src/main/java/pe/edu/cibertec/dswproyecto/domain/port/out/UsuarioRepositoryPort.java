
package pe.edu.cibertec.dswproyecto.domain.port.out;

import pe.edu.cibertec.dswproyecto.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Optional<Usuario> buscarPorId(Integer id);

    Optional<Usuario> buscarPorUsername(String username);

    List<Usuario> listarTodos();

    Usuario guardar(Usuario usuario);

    boolean existePorUsername(String username);
}
