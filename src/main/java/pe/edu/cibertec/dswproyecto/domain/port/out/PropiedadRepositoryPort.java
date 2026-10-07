package pe.edu.cibertec.dswproyecto.domain.port.out;

import pe.edu.cibertec.dswproyecto.domain.model.Propiedad;

import java.util.List;
import java.util.Optional;

public interface PropiedadRepositoryPort {

    List<Propiedad> listar();

    Optional<Propiedad> buscarPorId(Integer id);

    Propiedad guardar(Propiedad propiedad);

    void eliminar(Integer id);
}