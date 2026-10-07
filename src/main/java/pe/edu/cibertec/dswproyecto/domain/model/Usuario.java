package pe.edu.cibertec.dswproyecto.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private Integer id;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String correo;
    private RolUsuario rol;
    private Boolean activo;
}
