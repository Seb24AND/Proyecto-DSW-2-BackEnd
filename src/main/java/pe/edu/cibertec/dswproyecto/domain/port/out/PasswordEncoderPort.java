package pe.edu.cibertec.dswproyecto.domain.port.out;

public interface PasswordEncoderPort {

    String encode(String password);

    boolean matches(String password, String encodedPassword);
}