package pe.edu.cibertec.dswproyecto.domain.port.in;

import pe.edu.cibertec.dswproyecto.domain.model.Visita;

import java.util.List;

public interface VisitaUseCase {

    List<Visita> listar();

    Visita buscarPorId(Integer id);

    Visita guardar(Visita visita);

    void eliminar(Integer id);
}
