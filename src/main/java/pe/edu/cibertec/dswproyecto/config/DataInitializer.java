package pe.edu.cibertec.dswproyecto.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.Cliente;
import pe.edu.cibertec.dswproyecto.domain.model.RolUsuario;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.out.ClienteRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.PasswordEncoderPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public void run(String... args) {

        crearUsuario(
                "admin",
                "admin123",
                "Administrador",
                "Arteco",
                "70000001",
                "900000001",
                "admin@arteco.com",
                RolUsuario.ADMIN
        );

        crearUsuario(
                "asesor",
                "asesor123",
                "Carlos",
                "Perez",
                "70000002",
                "900000002",
                "carlos@arteco.com",
                RolUsuario.ASESOR
        );

        Usuario cliente = crearUsuario(
                "cliente",
                "cliente123",
                "Juan",
                "Perez",
                "70000003",
                "900000003",
                "juan@arteco.com",
                RolUsuario.CLIENTE
        );

        crearCliente(cliente);
    }

    private Usuario crearUsuario(
            String username,
            String password,
            String nombre,
            String apellido,
            String dni,
            String telefono,
            String correo,
            RolUsuario rol) {

        return usuarioRepositoryPort
                .buscarPorUsername(username)
                .orElseGet(() -> usuarioRepositoryPort.guardar(
                        new Usuario(
                                null,
                                username,
                                passwordEncoderPort.encode(password),
                                nombre,
                                apellido,
                                dni,
                                telefono,
                                correo,
                                rol,
                                true
                        )
                ));
    }

    private void crearCliente(Usuario usuario) {

        if (!clienteRepositoryPort.buscarPorUsuarioId(usuario.getId()).isPresent()) {

            Cliente cliente = new Cliente(
                    null,
                    usuario.getId(),
                    LocalDateTime.now(),
                    true
            );

            clienteRepositoryPort.guardar(cliente);
        }
    }
}
