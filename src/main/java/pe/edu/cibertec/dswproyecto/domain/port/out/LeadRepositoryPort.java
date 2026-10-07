package pe.edu.cibertec.dswproyecto.domain.port.out;

import pe.edu.cibertec.dswproyecto.domain.model.Lead;

import java.util.List;
import java.util.Optional;

public interface LeadRepositoryPort {

    List<Lead> listar();

    Optional<Lead> buscarPorId(Integer id);

    Lead guardar(Lead lead);

    void eliminar(Integer id);
}
