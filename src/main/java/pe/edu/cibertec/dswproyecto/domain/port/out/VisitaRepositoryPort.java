package pe.edu.cibertec.dswproyecto.domain.port.out;

import pe.edu.cibertec.dswproyecto.domain.model.Visita;

import java.util.List;
import java.util.Optional;

public interface VisitaRepositoryPort {

    List<Visita> listar();

    Optional<Visita> buscarPorId(Integer id);

    Visita guardar(Visita visita);

    void eliminar(Integer id);
}
