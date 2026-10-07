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
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.VentaRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.VentaResponse;
import pe.edu.cibertec.dswproyecto.domain.model.Venta;
import pe.edu.cibertec.dswproyecto.domain.port.in.VentaUseCase;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaUseCase ventaUseCase;

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listar() {

        List<VentaResponse> ventas = ventaUseCase.listar()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {

        try {
            return ResponseEntity.ok(
                    toResponse(ventaUseCase.buscarPorId(id))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MensajeResponse(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody VentaRequest request) {

        try {
            Venta guardada = ventaUseCase.guardar(toDomain(null, request));

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
            @Valid @RequestBody VentaRequest request) {

        try {
            Venta actualizada = ventaUseCase.guardar(toDomain(id, request));

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
            ventaUseCase.eliminar(id);
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

    private Venta toDomain(Integer id, VentaRequest request) {
        return new Venta(
                id,
                request.getSeparacionId(),
                null,
                null,
                request.getAsesorId(),
                request.getPrecioVenta(),
                request.getFechaVenta(),
                request.getMetodoPago(),
                request.getEstado(),
                request.getObservacion(),
                request.getActivo() != null ? request.getActivo() : true
        );
    }

    private VentaResponse toResponse(Venta venta) {
        return new VentaResponse(
                venta.getId(),
                venta.getSeparacionId(),
                venta.getLeadId(),
                venta.getPropiedadId(),
                venta.getAsesorId(),
                venta.getPrecioVenta(),
                venta.getFechaVenta(),
                venta.getMetodoPago(),
                venta.getEstado(),
                venta.getObservacion(),
                venta.getActivo()
        );
    }
}
