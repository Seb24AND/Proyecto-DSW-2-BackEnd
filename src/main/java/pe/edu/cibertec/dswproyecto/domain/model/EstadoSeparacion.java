package pe.edu.cibertec.dswproyecto.domain.model;

public enum EstadoSeparacion {

    PENDIENTE,
    CONFIRMADA,
    CANCELADA,
    VENCIDA;

    public boolean esActiva() {
        return this == PENDIENTE || this == CONFIRMADA;
    }

    public boolean puedeCambiarA(EstadoSeparacion nuevo) {
        if (this == nuevo) {
            return true;
        }
        return switch (this) {
            case PENDIENTE -> true;
            case CONFIRMADA -> nuevo == CANCELADA || nuevo == VENCIDA;
            case CANCELADA, VENCIDA -> false;
        };
    }
}
