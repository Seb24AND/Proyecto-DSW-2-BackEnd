package pe.edu.cibertec.dswproyecto.adapter.out.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;

@Component
@RequiredArgsConstructor
public class UsuarioUserDetailsService implements UserDetailsService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Usuario usuario = usuarioRepositoryPort
                .buscarPorUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .disabled(!usuario.getActivo())
                .build();
    }
}
