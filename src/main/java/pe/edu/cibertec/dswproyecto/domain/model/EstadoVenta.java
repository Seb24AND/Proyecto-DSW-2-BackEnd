package pe.edu.cibertec.dswproyecto.domain.model;

public enum EstadoVenta {

    INICIADA,
    EN_PROCESO,
    CONCRETADA,
    CANCELADA;

    public boolean esActiva() {
        return this != CANCELADA;
    }

    public boolean puedeCambiarA(EstadoVenta nuevo) {
        if (this == nuevo) {
            return true;
        }
        return switch (this) {
            case INICIADA -> true;
            case EN_PROCESO -> nuevo == CONCRETADA || nuevo == CANCELADA;
            case CONCRETADA, CANCELADA -> false;
        };
    }
}
