package pe.edu.cibertec.dswproyecto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.dswproyecto.domain.model.Cliente;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.in.ClienteUseCase;
import pe.edu.cibertec.dswproyecto.domain.port.out.ClienteRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.model.RolUsuario;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService implements ClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public List<Cliente> listar() {
        return clienteRepositoryPort.listar();
    }

    @Override
    public Cliente buscarPorId(Integer id) {
        return clienteRepositoryPort
                .buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    @Override
    public Cliente guardar(Cliente cliente) {

        Usuario usuario = usuarioRepositoryPort
                .buscarPorId(cliente.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (usuario.getRol() != RolUsuario.CLIENTE) {
            throw new IllegalArgumentException("El usuario no tiene rol CLIENTE");
        }

        clienteRepositoryPort
                .buscarPorUsuarioId(cliente.getUsuarioId())
                .ifPresent(existente -> {
                    if (!existente.getId().equals(cliente.getId())) {
                        throw new IllegalArgumentException("El usuario ya está asociado a un cliente");
                    }
                });

        if (cliente.getFechaRegistro() == null) {
            cliente.setFechaRegistro(LocalDateTime.now());
        }

        if (cliente.getActivo() == null) {
            cliente.setActivo(true);
        }

        return clienteRepositoryPort.guardar(cliente);
    }

    @Override
    public void eliminar(Integer id) {

        if (clienteRepositoryPort.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        clienteRepositoryPort.eliminar(id);
    }
}
