package pe.edu.cibertec.dswproyecto.domain.port.out;

import pe.edu.cibertec.dswproyecto.domain.model.Separacion;

import java.util.List;
import java.util.Optional;

public interface SeparacionRepositoryPort {

    List<Separacion> listar();

    Optional<Separacion> buscarPorId(Integer id);

    boolean existeActivaPorPropiedadId(Integer propiedadId);

    Separacion guardar(Separacion separacion);

    void eliminar(Integer id);
}
