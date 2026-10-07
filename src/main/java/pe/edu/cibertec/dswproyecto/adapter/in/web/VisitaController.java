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
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.VisitaRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.VisitaResponse;
import pe.edu.cibertec.dswproyecto.application.service.VisitaService;
import pe.edu.cibertec.dswproyecto.domain.model.Visita;

import java.util.List;

@RestController
@RequestMapping("/api/visitas")
@RequiredArgsConstructor
public class VisitaController {

    private final VisitaService visitaService;

    @GetMapping
    public ResponseEntity<List<VisitaResponse>> listar() {

        List<VisitaResponse> visitas = visitaService.listar()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(visitas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitaResponse> buscarPorId(@PathVariable Integer id) {

        try {
            return ResponseEntity.ok(
                    toResponse(visitaService.buscarPorId(id))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<VisitaResponse> guardar(
            @Valid @RequestBody VisitaRequest request) {

        try {
            Visita visita = new Visita(
                    null,
                    request.getLeadId(),
                    request.getFechaVisita(),
                    request.getEstado(),
                    request.getObservacion(),
                    request.getActivo() != null ? request.getActivo() : true
            );

            Visita guardada = visitaService.guardar(visita);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(toResponse(guardada));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitaResponse> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody VisitaRequest request) {

        try {
            visitaService.buscarPorId(id);

            Visita visita = new Visita(
                    id,
                    request.getLeadId(),
                    request.getFechaVisita(),
                    request.getEstado(),
                    request.getObservacion(),
                    request.getActivo() != null ? request.getActivo() : true
            );

            Visita actualizada = visitaService.guardar(visita);

            return ResponseEntity.ok(toResponse(actualizada));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        try {
            visitaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private VisitaResponse toResponse(Visita visita) {
        return new VisitaResponse(
                visita.getId(),
                visita.getLeadId(),
                visita.getFechaVisita(),
                visita.getEstado(),
                visita.getObservacion(),
                visita.getActivo()
        );
    }
}
