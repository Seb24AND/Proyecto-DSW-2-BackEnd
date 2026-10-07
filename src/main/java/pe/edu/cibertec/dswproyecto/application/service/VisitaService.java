package pe.edu.cibertec.dswproyecto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoVisita;
import pe.edu.cibertec.dswproyecto.domain.model.Visita;
import pe.edu.cibertec.dswproyecto.domain.port.in.VisitaUseCase;
import pe.edu.cibertec.dswproyecto.domain.port.out.LeadRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.VisitaRepositoryPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitaService implements VisitaUseCase {

    private final VisitaRepositoryPort visitaRepositoryPort;
    private final LeadRepositoryPort leadRepositoryPort;

    @Override
    public List<Visita> listar() {
        return visitaRepositoryPort.listar();
    }

    @Override
    public Visita buscarPorId(Integer id) {
        return visitaRepositoryPort
                .buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Visita no encontrada"));
    }

    @Override
    public Visita guardar(Visita visita) {

        leadRepositoryPort
                .buscarPorId(visita.getLeadId())
                .orElseThrow(() -> new IllegalArgumentException("Lead no encontrado"));

        if (visita.getEstado() == null) {
            visita.setEstado(EstadoVisita.PROGRAMADA);
        }

        if (visita.getActivo() == null) {
            visita.setActivo(true);
        }

        if (visita.getFechaVisita() == null) {
            throw new IllegalArgumentException("La fecha de visita es obligatoria");
        }

        return visitaRepositoryPort.guardar(visita);
    }

    @Override
    public void eliminar(Integer id) {

        if (!visitaRepositoryPort.buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException("Visita no encontrada");
        }

        visitaRepositoryPort.eliminar(id);
    }
}
