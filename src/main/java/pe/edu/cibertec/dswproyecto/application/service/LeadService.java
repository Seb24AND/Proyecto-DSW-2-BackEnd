package pe.edu.cibertec.dswproyecto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoLead;
import pe.edu.cibertec.dswproyecto.domain.model.Lead;
import pe.edu.cibertec.dswproyecto.domain.model.RolUsuario;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.in.LeadUseCase;
import pe.edu.cibertec.dswproyecto.domain.port.out.ClienteRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.LeadRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.PropiedadRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadService implements LeadUseCase {

    private final LeadRepositoryPort leadRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final PropiedadRepositoryPort propiedadRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public List<Lead> listar() {
        return leadRepositoryPort.listar();
    }

    @Override
    public Lead buscarPorId(Integer id) {
        return leadRepositoryPort
                .buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Lead no encontrado"));
    }

    @Override
    public Lead guardar(Lead lead) {

        clienteRepositoryPort
                .buscarPorId(lead.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        if (lead.getPropiedadId() != null) {
            propiedadRepositoryPort
                    .buscarPorId(lead.getPropiedadId())
                    .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
        }

        if (lead.getAsesorId() != null) {
            Usuario asesor = usuarioRepositoryPort
                    .buscarPorId(lead.getAsesorId())
                    .orElseThrow(() -> new IllegalArgumentException("Asesor no encontrado"));

            if (asesor.getRol() != RolUsuario.ASESOR) {
                throw new IllegalArgumentException("El usuario indicado no tiene rol de asesor");
            }
        }

        if (lead.getFechaRegistro() == null) {
            lead.setFechaRegistro(LocalDateTime.now());
        }

        if (lead.getEstado() == null) {
            lead.setEstado(EstadoLead.NUEVO);
        }

        if (lead.getActivo() == null) {
            lead.setActivo(true);
        }

        return leadRepositoryPort.guardar(lead);
    }

    @Override
    public void eliminar(Integer id) {

        if (leadRepositoryPort.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Lead no encontrado");
        }

        leadRepositoryPort.eliminar(id);
    }
}
