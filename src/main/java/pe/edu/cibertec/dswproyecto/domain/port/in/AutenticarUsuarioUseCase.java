package pe.edu.cibertec.dswproyecto.domain.port.in;

import pe.edu.cibertec.dswproyecto.domain.model.Usuario;

public interface AutenticarUsuarioUseCase {

    Usuario autenticar(String username, String password);
}