package pe.edu.cibertec.dswproyecto.domain.port.in;

import pe.edu.cibertec.dswproyecto.domain.model.Venta;

import java.util.List;

public interface VentaUseCase {

    List<Venta> listar();

    Venta buscarPorId(Integer id);

    Venta guardar(Venta venta);

    void eliminar(Integer id);
}
