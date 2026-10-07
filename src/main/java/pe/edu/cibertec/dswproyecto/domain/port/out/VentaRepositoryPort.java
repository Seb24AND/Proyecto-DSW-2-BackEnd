package pe.edu.cibertec.dswproyecto.domain.port.out;

import pe.edu.cibertec.dswproyecto.domain.model.Venta;

import java.util.List;
import java.util.Optional;

public interface VentaRepositoryPort {

    List<Venta> listar();

    Optional<Venta> buscarPorId(Integer id);

    boolean existeActivaPorSeparacionId(Integer separacionId);

    boolean existePorSeparacionId(Integer separacionId);

    Venta guardar(Venta venta);

    void eliminar(Integer id);
}
