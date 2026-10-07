package pe.edu.cibertec.dswproyecto.adapter.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.MensajeResponse;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.SeparacionRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.SeparacionResponse;
import pe.edu.cibertec.dswproyecto.domain.model.Separacion;
import pe.edu.cibertec.dswproyecto.domain.port.in.SeparacionUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/separaciones")
@RequiredArgsConstructor
public class SeparacionController {

    private final SeparacionUseCase separacionUseCase;

    @GetMapping
    public ResponseEntity<List<SeparacionResponse>> listar() {

        List<SeparacionResponse> separaciones = separacionUseCase.listar()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(separaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {

        try {
            return ResponseEntity.ok(
                    toResponse(separacionUseCase.buscarPorId(id))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MensajeResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody SeparacionRequest request) {

        try {
            Separacion guardada = separacionUseCase.guardar(toDomain(null, request));

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(toResponse(guardada));

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MensajeResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody SeparacionRequest request) {

        try {
            Separacion actualizada = separacionUseCase.guardar(toDomain(id, request));

            return ResponseEntity.ok(toResponse(actualizada));

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MensajeResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {

        try {
            separacionUseCase.eliminar(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MensajeResponse(e.getMessage()));

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MensajeResponse(e.getMessage()));
        }
    }

    private Separacion toDomain(Integer id, SeparacionRequest request) {
        return new Separacion(
                id,
                request.getLeadId(),
                request.getPropiedadId(),
                request.getAsesorId(),
                request.getMontoSeparacion(),
                request.getFechaSeparacion(),
                request.getFechaVencimiento(),
                request.getEstado(),
                request.getObservacion(),
                request.getActivo() != null ? request.getActivo() : true
        );
    }

    private SeparacionResponse toResponse(Separacion separacion) {
        return new SeparacionResponse(
                separacion.getId(),
                separacion.getLeadId(),
                separacion.getPropiedadId(),
                separacion.getAsesorId(),
                separacion.getMontoSeparacion(),
                separacion.getFechaSeparacion(),
                separacion.getFechaVencimiento(),
                separacion.getEstado(),
                separacion.getObservacion(),
                separacion.getActivo()
        );
    }
}
