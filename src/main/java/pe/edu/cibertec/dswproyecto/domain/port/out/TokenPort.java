package pe.edu.cibertec.dswproyecto.domain.port.out;

import pe.edu.cibertec.dswproyecto.domain.model.Usuario;

public interface TokenPort {

    String generarToken(Usuario usuario);
}