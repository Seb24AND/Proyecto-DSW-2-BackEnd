package pe.edu.cibertec.dswproyecto.domain.port.in;

import pe.edu.cibertec.dswproyecto.domain.model.Cliente;

import java.util.List;

public interface ClienteUseCase {

    List<Cliente> listar();

    Cliente buscarPorId(Integer id);

    Cliente guardar(Cliente cliente);

    void eliminar(Integer id);
}