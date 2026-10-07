package pe.edu.cibertec.dswproyecto.domain.port.in;

import pe.edu.cibertec.dswproyecto.domain.model.Propiedad;

import java.util.List;

public interface PropiedadUseCase {

    List<Propiedad> listar();

    Propiedad buscarPorId(Integer id);

    Propiedad guardar(Propiedad propiedad);

    void eliminar(Integer id);
}