package pe.edu.cibertec.dswproyecto.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.LoginRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.LoginResponse;
import pe.edu.cibertec.dswproyecto.application.service.AutenticacionService;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticacionService autenticacionService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        try {
            Usuario usuario = autenticacionService.autenticar(
                    request.getUsername(),
                    request.getPassword()
            );

            String token = autenticacionService.generarToken(usuario);

            return ResponseEntity.ok(
                    new LoginResponse(
                            usuario.getUsername(),
                            usuario.getRol(),
                            token,
                            "Login correcto"
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(
                            null,
                            null,
                            null,
                            e.getMessage()
                    ));
        }
    }
}