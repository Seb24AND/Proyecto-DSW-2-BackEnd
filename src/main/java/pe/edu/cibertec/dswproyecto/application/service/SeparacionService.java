package pe.edu.cibertec.dswproyecto.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoPropiedad;
import pe.edu.cibertec.dswproyecto.domain.model.EstadoSeparacion;
import pe.edu.cibertec.dswproyecto.domain.model.Lead;
import pe.edu.cibertec.dswproyecto.domain.model.Propiedad;
import pe.edu.cibertec.dswproyecto.domain.model.RolUsuario;
import pe.edu.cibertec.dswproyecto.domain.model.Separacion;
import pe.edu.cibertec.dswproyecto.domain.model.Usuario;
import pe.edu.cibertec.dswproyecto.domain.port.in.SeparacionUseCase;
import pe.edu.cibertec.dswproyecto.domain.port.out.LeadRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.PropiedadRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.SeparacionRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.UsuarioRepositoryPort;
import pe.edu.cibertec.dswproyecto.domain.port.out.VentaRepositoryPort;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeparacionService implements SeparacionUseCase {

    private static final int DIAS_VENCIMIENTO_POR_DEFECTO = 7;

    private final SeparacionRepositoryPort separacionRepositoryPort;
    private final VentaRepositoryPort ventaRepositoryPort;
    private final LeadRepositoryPort leadRepositoryPort;
    private final PropiedadRepositoryPort propiedadRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    @Override
    public List<Separacion> listar() {
        return separacionRepositoryPort.listar();
    }

    @Override
    public Separacion buscarPorId(Integer id) {
        return separacionRepositoryPort
                .buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Separación no encontrada"));
    }

    @Override
    @Transactional
    public Separacion guardar(Separacion separacion) {
        return separacion.getId() == null
                ? registrar(separacion)
                : actualizar(separacion);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {

        Separacion separacion = buscarPorId(id);

        if (ventaRepositoryPort.existePorSeparacionId(id)) {
            throw new IllegalStateException("No se puede eliminar una separación que tiene ventas registradas");
        }

        if (separacion.getEstado().esActiva()) {
            propiedadRepositoryPort
                    .buscarPorId(separacion.getPropiedadId())
                    .ifPresent(propiedad ->
                            cambiarEstadoPropiedad(propiedad, EstadoPropiedad.DISPONIBLE)
                    );
        }

        separacionRepositoryPort.eliminar(id);
    }

    private Separacion registrar(Separacion separacion) {

        Lead lead = obtenerLead(separacion.getLeadId());

        if (!Boolean.TRUE.equals(lead.getActivo())) {
            throw new IllegalStateException("El lead se encuentra inactivo");
        }

        if (lead.getPropiedadId() == null) {
            throw new IllegalStateException("El lead no tiene una propiedad asignada");
        }

        if (lead.getAsesorId() == null) {
            throw new IllegalStateException("El lead no tiene un asesor asignado");
        }

        separacion.setPropiedadId(lead.getPropiedadId());
        separacion.setAsesorId(lead.getAsesorId());

        Propiedad propiedad = obtenerPropiedad(separacion.getPropiedadId());

        if (separacion.getEstado() == null) {
            separacion.setEstado(EstadoSeparacion.PENDIENTE);
        }

        if (separacion.getFechaSeparacion() == null) {
            separacion.setFechaSeparacion(LocalDate.now());
        }

        completarDatosComunes(separacion);
        validarAsesor(separacion.getAsesorId());
        validarMontoYFechas(separacion, propiedad);

        if (!separacion.getEstado().esActiva()) {
            throw new IllegalStateException(
                    "Una nueva separación debe registrarse como PENDIENTE o CONFIRMADA"
            );
        }

        if (!Boolean.TRUE.equals(propiedad.getActivo())
                || propiedad.getEstado() != EstadoPropiedad.DISPONIBLE) {
            throw new IllegalStateException("La propiedad no está disponible para separar");
        }

        if (separacionRepositoryPort.existeActivaPorPropiedadId(propiedad.getId())) {
            throw new IllegalStateException("La propiedad ya tiene una separación activa");
        }

        cambiarEstadoPropiedad(propiedad, EstadoPropiedad.SEPARADA);

        return separacionRepositoryPort.guardar(separacion);
    }

    private Separacion actualizar(Separacion separacion) {

        Separacion existente = buscarPorId(separacion.getId());

        if (!existente.getLeadId().equals(separacion.getLeadId())) {
            throw new IllegalStateException("No se puede cambiar el lead de una separación");
        }

        if (ventaRepositoryPort.existeActivaPorSeparacionId(existente.getId())) {
            throw new IllegalStateException(
                    "No se puede modificar una separación que tiene una venta activa"
            );
        }

        Lead lead = obtenerLead(existente.getLeadId());

        if (!Boolean.TRUE.equals(lead.getActivo())) {
            throw new IllegalStateException("El lead se encuentra inactivo");
        }

        separacion.setPropiedadId(existente.getPropiedadId());
        separacion.setAsesorId(existente.getAsesorId());

        Propiedad propiedad = obtenerPropiedad(existente.getPropiedadId());

        if (separacion.getEstado() == null) {
            separacion.setEstado(existente.getEstado());
        }

        if (separacion.getFechaSeparacion() == null) {
            separacion.setFechaSeparacion(existente.getFechaSeparacion());
        }

        if (separacion.getFechaVencimiento() == null) {
            separacion.setFechaVencimiento(existente.getFechaVencimiento());
        }

        completarDatosComunes(separacion);
        validarAsesor(separacion.getAsesorId());
        validarMontoYFechas(separacion, propiedad);

        EstadoSeparacion estadoAnterior = existente.getEstado();
        EstadoSeparacion estadoNuevo = separacion.getEstado();

        if (!estadoAnterior.puedeCambiarA(estadoNuevo)) {
            throw new IllegalStateException(
                    "No se puede cambiar la separación de "
                            + estadoAnterior
                            + " a "
                            + estadoNuevo
            );
        }

        if (estadoAnterior.esActiva() && !estadoNuevo.esActiva()) {
            cambiarEstadoPropiedad(propiedad, EstadoPropiedad.DISPONIBLE);
        }

        return separacionRepositoryPort.guardar(separacion);
    }

    private void completarDatosComunes(Separacion separacion) {

        if (separacion.getFechaVencimiento() == null) {
            separacion.setFechaVencimiento(
                    separacion.getFechaSeparacion().plusDays(DIAS_VENCIMIENTO_POR_DEFECTO)
            );
        }

        if (separacion.getActivo() == null) {
            separacion.setActivo(true);
        }
    }

    private void validarMontoYFechas(
            Separacion separacion,
            Propiedad propiedad) {

        if (separacion.getMontoSeparacion() == null
                || separacion.getMontoSeparacion().signum() <= 0) {
            throw new IllegalArgumentException(
                    "El monto de separación debe ser mayor a cero"
            );
        }

        if (separacion.getMontoSeparacion().compareTo(propiedad.getPrecio()) > 0) {
            throw new IllegalArgumentException(
                    "El monto de separación no puede superar el precio de la propiedad"
            );
        }

        if (separacion.getFechaVencimiento()
                .isBefore(separacion.getFechaSeparacion())) {
            throw new IllegalArgumentException(
                    "La fecha de vencimiento no puede ser anterior a la fecha de separación"
            );
        }
    }

    private void validarAsesor(Integer asesorId) {

        if (asesorId == null) {
            throw new IllegalArgumentException("El asesor es obligatorio");
        }

        Usuario asesor = usuarioRepositoryPort
                .buscarPorId(asesorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Asesor no encontrado")
                );

        if (asesor.getRol() != RolUsuario.ASESOR) {
            throw new IllegalArgumentException(
                    "El usuario indicado no tiene rol de asesor"
            );
        }
    }

    private Lead obtenerLead(Integer leadId) {
        return leadRepositoryPort
                .buscarPorId(leadId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Lead no encontrado")
                );
    }

    private Propiedad obtenerPropiedad(Integer propiedadId) {
        return propiedadRepositoryPort
                .buscarPorId(propiedadId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Propiedad no encontrada")
                );
    }

    private void cambiarEstadoPropiedad(
            Propiedad propiedad,
            EstadoPropiedad estado) {

        propiedad.setEstado(estado);
        propiedadRepositoryPort.guardar(propiedad);
    }
}
