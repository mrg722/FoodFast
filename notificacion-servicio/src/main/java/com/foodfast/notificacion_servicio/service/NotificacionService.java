package com.foodfast.notificacion_servicio.service;

import com.foodfast.notificacion_servicio.dto.NotificacionRequest;
import com.foodfast.notificacion_servicio.dto.NotificacionResponse;
import com.foodfast.notificacion_servicio.exception.RecursoNoEncontradoException;
import com.foodfast.notificacion_servicio.exception.ReglaNegocioException;
import com.foodfast.notificacion_servicio.model.EstadoNotificacion;
import com.foodfast.notificacion_servicio.model.Notificacion;
import com.foodfast.notificacion_servicio.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionService.class);
    private final NotificacionRepository notificacionRepository;

    public List<NotificacionResponse> listar() {
        log.info("Listando notificaciones");
        return notificacionRepository.findAll().stream().map(this::toResponse).toList();
    }

    public NotificacionResponse buscarPorId(Long id) {
        log.info("Buscando notificacion id={}", id);
        return toResponse(obtenerNotificacion(id));
    }

    public List<NotificacionResponse> listarPorCliente(Long clienteId) {
        log.info("Listando notificaciones por clienteId={}", clienteId);
        return notificacionRepository.findByClienteId(clienteId).stream().map(this::toResponse).toList();
    }

    public List<NotificacionResponse> listarPorEstado(EstadoNotificacion estado) {
        log.info("Listando notificaciones por estado={}", estado);
        return notificacionRepository.findByEstado(estado).stream().map(this::toResponse).toList();
    }

    @Transactional
    public NotificacionResponse crear(NotificacionRequest request) {
        log.info("Creando notificacion clienteId={} tipo={}", request.getClienteId(), request.getTipo());
        Notificacion notificacion = Notificacion.builder()
                .clienteId(request.getClienteId())
                .tipo(request.getTipo())
                .canal(request.getCanal())
                .estado(EstadoNotificacion.PENDIENTE)
                .titulo(request.getTitulo())
                .mensaje(request.getMensaje())
                .referenciaTipo(request.getReferenciaTipo())
                .referenciaId(request.getReferenciaId())
                .fechaCreacion(LocalDateTime.now())
                .build();
        return toResponse(notificacionRepository.save(notificacion));
    }

    @Transactional
    public NotificacionResponse actualizar(Long id, NotificacionRequest request) {
        Notificacion notificacion = obtenerNotificacion(id);
        log.info("Actualizando notificacion id={}", id);
        if (notificacion.getEstado() == EstadoNotificacion.ENVIADA || notificacion.getEstado() == EstadoNotificacion.LEIDA) {
            throw new ReglaNegocioException("No se puede editar una notificacion enviada o leida");
        }

        notificacion.setClienteId(request.getClienteId());
        notificacion.setTipo(request.getTipo());
        notificacion.setCanal(request.getCanal());
        notificacion.setTitulo(request.getTitulo());
        notificacion.setMensaje(request.getMensaje());
        notificacion.setReferenciaTipo(request.getReferenciaTipo());
        notificacion.setReferenciaId(request.getReferenciaId());

        return toResponse(notificacionRepository.save(notificacion));
    }

    @Transactional
    public NotificacionResponse enviar(Long id) {
        Notificacion notificacion = obtenerNotificacion(id);
        log.info("Enviando notificacion id={} por canal={}", id, notificacion.getCanal());
        if (notificacion.getEstado() == EstadoNotificacion.LEIDA) {
            throw new ReglaNegocioException("No se puede reenviar una notificacion ya leida");
        }
        notificacion.setEstado(EstadoNotificacion.ENVIADA);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setErrorEnvio(null);
        return toResponse(notificacionRepository.save(notificacion));
    }

    @Transactional
    public NotificacionResponse marcarComoLeida(Long id) {
        Notificacion notificacion = obtenerNotificacion(id);
        log.info("Marcando como leida notificacion id={}", id);
        if (notificacion.getEstado() == EstadoNotificacion.PENDIENTE) {
            throw new ReglaNegocioException("No se puede marcar como leida una notificacion pendiente de envio");
        }
        notificacion.setEstado(EstadoNotificacion.LEIDA);
        notificacion.setFechaLectura(LocalDateTime.now());
        return toResponse(notificacionRepository.save(notificacion));
    }

    @Transactional
    public void eliminar(Long id) {
        Notificacion notificacion = obtenerNotificacion(id);
        log.info("Eliminando notificacion id={}", id);
        notificacionRepository.delete(notificacion);
    }

    private Notificacion obtenerNotificacion(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Notificacion no encontrada con id: " + id));
    }

    private NotificacionResponse toResponse(Notificacion notificacion) {
        return NotificacionResponse.builder()
                .id(notificacion.getId())
                .clienteId(notificacion.getClienteId())
                .tipo(notificacion.getTipo())
                .canal(notificacion.getCanal())
                .estado(notificacion.getEstado())
                .titulo(notificacion.getTitulo())
                .mensaje(notificacion.getMensaje())
                .referenciaTipo(notificacion.getReferenciaTipo())
                .referenciaId(notificacion.getReferenciaId())
                .fechaCreacion(notificacion.getFechaCreacion())
                .fechaEnvio(notificacion.getFechaEnvio())
                .fechaLectura(notificacion.getFechaLectura())
                .errorEnvio(notificacion.getErrorEnvio())
                .build();
    }
}
