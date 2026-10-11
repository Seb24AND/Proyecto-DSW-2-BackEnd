
package pe.edu.cibertec.dswproyecto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.dswproyecto.domain.model.Cliente;
import pe.edu.cibertec.dswproyecto.domain.model.RolUsuario;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.in.AutenticarUsuarioUseCase;
import pe.edu.cibertec.dswproyecto.domain.port.out.ClienteRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.PasswordEncoderPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.TokenPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AutenticacionService implements AutenticarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenPort tokenPort;

    @Override
    public Usuario autenticar(String username, String password) {

        Usuario usuario = usuarioRepositoryPort
                .buscarPorUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario o contraseña incorrectos"
                        ));

        if (!passwordEncoderPort.matches(
                password, usuario.getPassword())) {
            throw new IllegalArgumentException(
                    "Usuario o contraseña incorrectos"
            );
        }

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new IllegalArgumentException(
                    "El usuario se encuentra inactivo"
            );
        }

        return usuario;
    }

    @Transactional
    public Usuario registrarCliente(
            String username,
            String password,
            String nombre,
            String apellido,
            String dni,
            String telefono,
            String correo) {

        if (usuarioRepositoryPort.existePorUsername(username)) {
            throw new IllegalArgumentException(
                    "El username ya está registrado"
            );
        }

        Usuario nuevoUsuario = new Usuario(
                null,
                username,
                passwordEncoderPort.encode(password),
                nombre,
                apellido,
                dni,
                telefono,
                correo,
                RolUsuario.CLIENTE,
                true
        );

        Usuario usuarioGuardado =
                usuarioRepositoryPort.guardar(nuevoUsuario);

        Cliente nuevoCliente = new Cliente(
                null,
                usuarioGuardado.getId(),
                LocalDateTime.now(),
                true
        );

        clienteRepositoryPort.guardar(nuevoCliente);

        return usuarioGuardado;
    }

    public String generarToken(Usuario usuario) {
        return tokenPort.generarToken(usuario);
    }
}
