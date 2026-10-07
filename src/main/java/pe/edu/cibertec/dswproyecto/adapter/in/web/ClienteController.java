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
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.ClienteRequest;
import pe.edu.cibertec.dswproyecto.adapter.in.web.dto.ClienteResponse;
import pe.edu.cibertec.dswproyecto.application.service.ClienteService;
import pe.edu.cibertec.dswproyecto.domain.model.Cliente;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        List<ClienteResponse> clientes = clienteService.listar()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(
                    toResponse(clienteService.buscarPorId(id))
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> guardar(
            @Valid @RequestBody ClienteRequest request) {

        try {
            Cliente cliente = new Cliente(
                    null,
                    request.getUsuarioId(),
                    null,
                    request.getActivo() != null ? request.getActivo() : true
            );

            Cliente guardado = clienteService.guardar(cliente);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(toResponse(guardado));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteRequest request) {

        try {
            Cliente existente = clienteService.buscarPorId(id);

            Cliente cliente = new Cliente(
                    id,
                    request.getUsuarioId(),
                    existente.getFechaRegistro(),
                    request.getActivo() != null ? request.getActivo() : true
            );

            Cliente actualizado = clienteService.guardar(cliente);

            return ResponseEntity.ok(toResponse(actualizado));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        try {
            clienteService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getUsuarioId(),
                cliente.getFechaRegistro(),
                cliente.getActivo()
        );
    }
}
