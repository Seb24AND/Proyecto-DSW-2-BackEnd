
package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

import pe.edu.cibertec.dswproyecto.domain.model.RolUsuario;

public record UsuarioResponse(
        Integer id,
        String username,
        String nombre,
        String apellido,
        String dni,
        String telefono,
        String correo,
        RolUsuario rol,
        Boolean activo
) {
}
