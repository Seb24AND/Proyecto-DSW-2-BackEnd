package pe.edu.cibertec.dswproyecto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.dswproyecto.domain.model.Propiedad;
import pe.edu.cibertec.dswproyecto.domain.port.in.PropiedadUseCase;
import pe.edu.cibertec.dswproyecto.domain.port.out.PropiedadRepositoryPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropiedadService implements PropiedadUseCase {

    private final PropiedadRepositoryPort propiedadRepositoryPort;

    @Override
    public List<Propiedad> listar() {
        return propiedadRepositoryPort.listar();
    }

    @Override
    public Propiedad buscarPorId(Integer id) {
        return propiedadRepositoryPort
                .buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
    }

    @Override
    public Propiedad guardar(Propiedad propiedad) {
        return propiedadRepositoryPort.guardar(propiedad);
    }

    @Override
    public void eliminar(Integer id) {

        if (!propiedadRepositoryPort.buscarPorId(id).isPresent()) {
            throw new IllegalArgumentException("Propiedad no encontrada");
        }

        propiedadRepositoryPort.eliminar(id);
    }
}