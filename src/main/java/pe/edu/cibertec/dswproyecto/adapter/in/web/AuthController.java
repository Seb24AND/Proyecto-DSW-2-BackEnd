
package pe.edu.cibertec.dswproyecto.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.LoginRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.LoginResponse;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.RegistroRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.RegistroResponse;
import pe.edu.cibertec.dswproyecto.application.service.AutenticacionService;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticacionService autenticacionService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        try {
            Usuario usuario = autenticacionService.autenticar(
                    request.getUsername(),
                    request.getPassword()
            );

            String token = autenticacionService.generarToken(usuario);

            return ResponseEntity.ok(
                    new LoginResponse(
                            usuario.getUsername(),
                            usuario.getRol().name(),
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

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody RegistroRequest request) {

        try {
            Usuario usuario = autenticacionService.registrarCliente(
                    request.username(),
                    request.password(),
                    request.nombre(),
                    request.apellido(),
                    request.dni(),
                    request.telefono(),
                    request.correo()
            );

            RegistroResponse respuesta = new RegistroResponse(
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getRol().name(),
                    "Cliente registrado correctamente"
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(respuesta);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}
