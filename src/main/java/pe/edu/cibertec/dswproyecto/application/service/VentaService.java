package pe.edu.cibertec.dswproyecto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoLead;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoPropiedad;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoSeparacion;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoVenta;
import pe.edu.cibertec.dswproyecto.domain.model.Propiedad;
import pe.edu.cibertec.dswproyecto.domain.model.RolUsuario;
import pe.edu.cibertec.dswproyecto.domain.model.Separacion;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.model.Venta;
import pe.edu.cibertec.dswproyecto.domain.port.in.VentaUseCase;
import pe.edu.cibertec.dswproyecto.domain.port.out.LeadRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.PropiedadRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.SeparacionRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.VentaRepositoryPort;

import java.time.LocalDate;
import java.util.List;

/**
 * Reglas de negocio de las ventas:
 * - Toda venta nace de una separación CONFIRMADA (lead y propiedad se copian de ella).
 * - Una separación solo puede tener una venta activa.
 * - Venta activa (INICIADA/EN_PROCESO/CONCRETADA) -> propiedad VENDIDA.
 * - Venta CONCRETADA -> lead CONVERTIDO.
 * - Venta CANCELADA -> propiedad DISPONIBLE y la separación pasa a CANCELADA.
 */
@Service
@RequiredArgsConstructor
public class VentaService implements VentaUseCase {

    private final VentaRepositoryPort ventaRepositoryPort;
    private final SeparacionRepositoryPort separacionRepositoryPort;
    private final PropiedadRepositoryPort propiedadRepositoryPort;
    private final LeadRepositoryPort leadRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public List<Venta> listar() {
        return ventaRepositoryPort.listar();
    }

    @Override
    public Venta buscarPorId(Integer id) {
        return ventaRepositoryPort
                .buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
    }

    @Override
    @Transactional
    public Venta guardar(Venta venta) {
        return venta.getId() == null
                ? registrar(venta)
                : actualizar(venta);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {

        Venta venta = buscarPorId(id);

        if (venta.getEstado() == EstadoVenta.CONCRETADA) {
            throw new IllegalStateException("No se puede eliminar una venta concretada");
        }

        // Si la venta seguía activa, la separación sigue CONFIRMADA: la propiedad vuelve a SEPARADA
        if (venta.getEstado().esActiva()) {
            propiedadRepositoryPort
                    .buscarPorId(venta.getPropiedadId())
                    .ifPresent(propiedad -> cambiarEstadoPropiedad(propiedad, EstadoPropiedad.SEPARADA));
        }

        ventaRepositoryPort.eliminar(id);
    }

    private Venta registrar(Venta venta) {

        if (venta.getSeparacionId() == null) {
            throw new IllegalArgumentException("La separación es obligatoria para registrar una venta");
        }

        Separacion separacion = obtenerSeparacion(venta.getSeparacionId());

        if (separacion.getEstado() != EstadoSeparacion.CONFIRMADA) {
            throw new IllegalStateException("Solo se puede registrar una venta a partir de una separación CONFIRMADA");
        }

        if (ventaRepositoryPort.existeActivaPorSeparacionId(separacion.getId())) {
            throw new IllegalStateException("La separación ya tiene una venta activa");
        }

        Propiedad propiedad = obtenerPropiedad(separacion.getPropiedadId());

        venta.setLeadId(separacion.getLeadId());
        venta.setPropiedadId(separacion.getPropiedadId());

        if (venta.getAsesorId() == null) {
            venta.setAsesorId(separacion.getAsesorId());
        }

        if (venta.getPrecioVenta() == null) {
            venta.setPrecioVenta(propiedad.getPrecio());
        }

        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(LocalDate.now());
        }

        if (venta.getEstado() == null) {
            venta.setEstado(EstadoVenta.INICIADA);
        }

        if (venta.getActivo() == null) {
            venta.setActivo(true);
        }

        validarDatos(venta, separacion);

        if (!venta.getEstado().esActiva()) {
            throw new IllegalStateException("No se puede registrar una venta en estado CANCELADA");
        }

        cambiarEstadoPropiedad(propiedad, EstadoPropiedad.VENDIDA);

        if (venta.getEstado() == EstadoVenta.CONCRETADA) {
            marcarLeadComoConvertido(venta.getLeadId());
        }

        return ventaRepositoryPort.guardar(venta);
    }

    private Venta actualizar(Venta venta) {

        Venta existente = buscarPorId(venta.getId());

        if (venta.getSeparacionId() != null
                && !existente.getSeparacionId().equals(venta.getSeparacionId())) {
            throw new IllegalStateException("No se puede cambiar la separación de una venta");
        }

        Separacion separacion = obtenerSeparacion(existente.getSeparacionId());

        // Datos que siempre se heredan de la venta original
        venta.setSeparacionId(existente.getSeparacionId());
        venta.setLeadId(existente.getLeadId());
        venta.setPropiedadId(existente.getPropiedadId());

        if (venta.getAsesorId() == null) {
            venta.setAsesorId(existente.getAsesorId());
        }

        if (venta.getPrecioVenta() == null) {
            venta.setPrecioVenta(existente.getPrecioVenta());
        }

        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(existente.getFechaVenta());
        }

        if (venta.getMetodoPago() == null) {
            venta.setMetodoPago(existente.getMetodoPago());
        }

        if (venta.getEstado() == null) {
            venta.setEstado(existente.getEstado());
        }

        if (venta.getActivo() == null) {
            venta.setActivo(existente.getActivo());
        }

        validarDatos(venta, separacion);

        EstadoVenta estadoAnterior = existente.getEstado();
        EstadoVenta estadoNuevo = venta.getEstado();

        if (!estadoAnterior.puedeCambiarA(estadoNuevo)) {
            throw new IllegalStateException(
                    "No se puede cambiar la venta de " + estadoAnterior + " a " + estadoNuevo);
        }

        if (estadoAnterior != estadoNuevo) {
            aplicarCambioDeEstado(venta, separacion);
        }

        return ventaRepositoryPort.guardar(venta);
    }

    private void aplicarCambioDeEstado(Venta venta, Separacion separacion) {

        switch (venta.getEstado()) {

            case CONCRETADA -> marcarLeadComoConvertido(venta.getLeadId());

            case CANCELADA -> {
                propiedadRepositoryPort
                        .buscarPorId(venta.getPropiedadId())
                        .ifPresent(propiedad -> cambiarEstadoPropiedad(propiedad, EstadoPropiedad.DISPONIBLE));

                separacion.setEstado(EstadoSeparacion.CANCELADA);
                separacionRepositoryPort.guardar(separacion);
            }

            default -> {
                // EN_PROCESO: no afecta a otras entidades
            }
        }
    }

    private void validarDatos(Venta venta, Separacion separacion) {

        if (venta.getMetodoPago() == null) {
            throw new IllegalArgumentException("El método de pago es obligatorio");
        }

        if (venta.getPrecioVenta() == null || venta.getPrecioVenta().signum() <= 0) {
            throw new IllegalArgumentException("El precio de venta debe ser mayor a cero");
        }

        if (venta.getFechaVenta().isBefore(separacion.getFechaSeparacion())) {
            throw new IllegalArgumentException("La fecha de venta no puede ser anterior a la fecha de separación");
        }

        validarAsesor(venta.getAsesorId());
    }

    private void validarAsesor(Integer asesorId) {

        if (asesorId == null) {
            return;
        }

        Usuario asesor = usuarioRepositoryPort
                .buscarPorId(asesorId)
                .orElseThrow(() -> new IllegalArgumentException("Asesor no encontrado"));

        if (asesor.getRol() != RolUsuario.ASESOR) {
            throw new IllegalArgumentException("El usuario indicado no tiene rol de asesor");
        }
    }

    private void marcarLeadComoConvertido(Integer leadId) {
        leadRepositoryPort
                .buscarPorId(leadId)
                .ifPresent(lead -> {
                    lead.setEstado(EstadoLead.CONVERTIDO);
                    leadRepositoryPort.guardar(lead);
                });
    }

    private Separacion obtenerSeparacion(Integer separacionId) {
        return separacionRepositoryPort
                .buscarPorId(separacionId)
                .orElseThrow(() -> new IllegalArgumentException("Separación no encontrada"));
    }

    private Propiedad obtenerPropiedad(Integer propiedadId) {
        return propiedadRepositoryPort
                .buscarPorId(propiedadId)
                .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada"));
    }

    private void cambiarEstadoPropiedad(Propiedad propiedad, EstadoPropiedad estado) {
        propiedad.setEstado(estado);
        propiedadRepositoryPort.guardar(propiedad);
    }
}
