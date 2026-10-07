package pe.edu.cibertec.dswproyecto.domain.port.out;

import pe.edu.cibertec.dswproyecto.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {

    List<Cliente> listar();

    Optional<Cliente> buscarPorId(Integer id);

    Optional<Cliente> buscarPorUsuarioId(Integer usuarioId);

    Cliente guardar(Cliente cliente);

    void eliminar(Integer id);
}
