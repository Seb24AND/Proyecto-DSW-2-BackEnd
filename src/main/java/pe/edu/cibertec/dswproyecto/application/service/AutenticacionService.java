package pe.edu.cibertec.dswproyecto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.in.AutenticarUsuarioUseCase;
import pe.edu.cibertec.dswproyecto.domain.port.out.PasswordEncoderPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.TokenPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;

@Service
@RequiredArgsConstructor
public class AutenticacionService implements AutenticarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenPort tokenPort;

    @Override
    public Usuario autenticar(String username, String password) {

        Usuario usuario = usuarioRepositoryPort
                .buscarPorUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrectos"));

        if (!passwordEncoderPort.matches(password, usuario.getPassword())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        if (!usuario.getActivo()) {
            throw new IllegalArgumentException("El usuario se encuentra inactivo");
        }

        return usuario;
    }

    public String generarToken(Usuario usuario) {
        return tokenPort.generarToken(usuario);
    }
}