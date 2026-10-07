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
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.PropiedadRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.PropiedadResponse;
import pe.edu.cibertec.dswproyecto.application.service.PropiedadService;
import pe.edu.cibertec.dswproyecto.domain.model.Propiedad;

import java.util.List;

@RestController
@RequestMapping("/api/propiedades")
@RequiredArgsConstructor
public class PropiedadController {

    private final PropiedadService propiedadService;

    @GetMapping
    public ResponseEntity<List<PropiedadResponse>> listar() {

        List<PropiedadResponse> propiedades = propiedadService.listar()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(propiedades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadResponse> buscarPorId(@PathVariable Integer id) {

        try {
            return ResponseEntity.ok(
                    toResponse(propiedadService.buscarPorId(id))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PropiedadResponse> guardar(
            @Valid @RequestBody PropiedadRequest request) {

        Propiedad propiedad = new Propiedad(
                null,
                request.getCodigo(),
                request.getTipo(),
                request.getDireccion(),
                request.getDistrito(),
                request.getArea(),
                request.getPrecio(),
                request.getEstado(),
                request.getActivo() != null ? request.getActivo() : true
        );

        Propiedad guardada = propiedadService.guardar(propiedad);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(guardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropiedadResponse> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PropiedadRequest request) {

        try {
            propiedadService.buscarPorId(id);

            Propiedad propiedad = new Propiedad(
                    id,
                    request.getCodigo(),
                    request.getTipo(),
                    request.getDireccion(),
                    request.getDistrito(),
                    request.getArea(),
                    request.getPrecio(),
                    request.getEstado(),
                    request.getActivo() != null ? request.getActivo() : true
            );

            Propiedad actualizada = propiedadService.guardar(propiedad);

            return ResponseEntity.ok(toResponse(actualizada));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        try {
            propiedadService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private PropiedadResponse toResponse(Propiedad propiedad) {
        return new PropiedadResponse(
                propiedad.getId(),
                propiedad.getCodigo(),
                propiedad.getTipo(),
                propiedad.getDireccion(),
                propiedad.getDistrito(),
                propiedad.getArea(),
                propiedad.getPrecio(),
                propiedad.getEstado(),
                propiedad.getActivo()
        );
    }
}
