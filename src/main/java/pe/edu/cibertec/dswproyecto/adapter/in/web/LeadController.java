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
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.LeadRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.LeadResponse;
import pe.edu.cibertec.dswproyecto.application.service.LeadService;
import pe.edu.cibertec.dswproyecto.domain.model.Lead;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @GetMapping
    public ResponseEntity<List<LeadResponse>> listar() {
        List<LeadResponse> leads = leadService.listar()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(leads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponse> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(
                    toResponse(leadService.buscarPorId(id))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<LeadResponse> guardar(
            @Valid @RequestBody LeadRequest request) {

        try {
            Lead lead = new Lead(
                    null,
                    request.getClienteId(),
                    request.getPropiedadId(),
                    request.getAsesorId(),
                    request.getEstado(),
                    null,
                    request.getObservacion(),
                    request.getActivo() != null ? request.getActivo() : true
            );

            Lead guardado = leadService.guardar(lead);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(toResponse(guardado));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadResponse> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody LeadRequest request) {

        try {
            Lead existente = leadService.buscarPorId(id);

            Lead lead = new Lead(
                    id,
                    request.getClienteId(),
                    request.getPropiedadId(),
                    request.getAsesorId(),
                    request.getEstado(),
                    existente.getFechaRegistro(),
                    request.getObservacion(),
                    request.getActivo() != null ? request.getActivo() : true
            );

            Lead actualizado = leadService.guardar(lead);

            return ResponseEntity.ok(toResponse(actualizado));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        try {
            leadService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private LeadResponse toResponse(Lead lead) {
        return new LeadResponse(
                lead.getId(),
                lead.getClienteId(),
                lead.getPropiedadId(),
                lead.getAsesorId(),
                lead.getEstado(),
                lead.getFechaRegistro(),
                lead.getObservacion(),
                lead.getActivo()
        );
    }
}
