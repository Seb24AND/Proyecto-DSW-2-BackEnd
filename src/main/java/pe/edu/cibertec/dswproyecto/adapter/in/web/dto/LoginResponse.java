package pe.edu.cibertec.dswproyecto.adapter.in.web.dto;

public record LoginResponse(
        String username,
        String rol,
        String token,
        String mensaje
) {
}
