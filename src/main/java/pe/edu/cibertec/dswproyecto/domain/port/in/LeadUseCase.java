package pe.edu.cibertec.dswproyecto.domain.port.in;

import pe.edu.cibertec.dswproyecto.domain.model.Lead;

import java.util.List;

public interface LeadUseCase {

    List<Lead> listar();

    Lead buscarPorId(Integer id);

    Lead guardar(Lead lead);

    void eliminar(Integer id);
}
