package pe.edu.cibertec.dswproyecto.domain.port.in;

import pe.edu.cibertec.dswproyecto.domain.model.Separacion;

import java.util.List;

public interface SeparacionUseCase {

    List<Separacion> listar();

    Separacion buscarPorId(Integer id);

    Separacion guardar(Separacion separacion);

    void eliminar(Integer id);
}
