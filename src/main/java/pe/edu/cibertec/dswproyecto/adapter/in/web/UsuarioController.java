
package pe.edu.cibertec.dswproyecto.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.UsuarioRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.UsuarioResponse;
import pe.edu.cibertec.dswproyecto.domain.model.RolUsuario;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.PasswordEncoderPort;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> usuarios =
                usuarioRepositoryPort.listarTodos()
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(
            @PathVariable Integer id) {

        return usuarioRepositoryPort.buscarPorId(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> registrar(
            @Valid @RequestBody UsuarioRequest request) {

        if (request.password() == null ||
                request.password().isBlank()) {
            return ResponseEntity.badRequest()
                    .body("La contraseña es obligatoria");
        }

        if (usuarioRepositoryPort.existePorUsername(
                request.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El username ya está registrado");
        }

        RolUsuario rol = request.rol() == null
                ? RolUsuario.ASESOR
                : request.rol();

        Usuario usuario = new Usuario(
                null,
                request.username(),
                passwordEncoderPort.encode(request.password()),
                request.nombre(),
                request.apellido(),
                request.dni(),
                request.telefono(),
                request.correo(),
                rol,
                true
        );

        Usuario guardado = usuarioRepositoryPort.guardar(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequest request) {

        var encontrado = usuarioRepositoryPort.buscarPorId(id);

        if (encontrado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario actual = encontrado.get();

        if (!actual.getUsername().equals(request.username())
                && usuarioRepositoryPort.existePorUsername(
                request.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El username ya está registrado");
        }

        String password = actual.getPassword();

        if (request.password() != null
                && !request.password().isBlank()) {
            password = passwordEncoderPort.encode(
                    request.password());
        }

        RolUsuario rol = request.rol() == null
                ? actual.getRol()
                : request.rol();

        Usuario actualizado = new Usuario(
                actual.getId(),
                request.username(),
                password,
                request.nombre(),
                request.apellido(),
                request.dni(),
                request.telefono(),
                request.correo(),
                rol,
                actual.getActivo()
        );

        Usuario guardado =
                usuarioRepositoryPort.guardar(actualizado);

        return ResponseEntity.ok(toResponse(guardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivar(
            @PathVariable Integer id) {

        var encontrado = usuarioRepositoryPort.buscarPorId(id);

        if (encontrado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario actual = encontrado.get();

        if (actual.getRol() == RolUsuario.ADMIN) {
            return ResponseEntity.badRequest()
                    .body("No se puede desactivar una cuenta ADMIN desde este endpoint");
        }

        Usuario desactivado = new Usuario(
                actual.getId(),
                actual.getUsername(),
                actual.getPassword(),
                actual.getNombre(),
                actual.getApellido(),
                actual.getDni(),
                actual.getTelefono(),
                actual.getCorreo(),
                actual.getRol(),
                false
        );

        usuarioRepositoryPort.guardar(desactivado);

        return ResponseEntity.noContent().build();
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDni(),
                usuario.getTelefono(),
                usuario.getCorreo(),
                usuario.getRol(),
                usuario.getActivo()
        );
    }
}
