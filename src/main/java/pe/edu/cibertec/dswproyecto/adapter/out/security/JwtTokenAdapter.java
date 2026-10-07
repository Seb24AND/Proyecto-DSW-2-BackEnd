package pe.edu.cibertec.dswproyecto.adapter.out.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.out.TokenPort;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtTokenAdapter implements TokenPort {

    private final JwtEncoder jwtEncoder;

    @Override
    public String generarToken(Usuario usuario) {


        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(usuario.getUsername())
                .claim("rol", usuario.getRol())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}