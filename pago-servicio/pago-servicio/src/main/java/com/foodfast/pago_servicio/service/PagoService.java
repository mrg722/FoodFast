package com.foodfast.pago_servicio.service;
 
import com.foodfast.pago_servicio.dto.PagoRequest;
import com.foodfast.pago_servicio.dto.PagoResponse;
import com.foodfast.pago_servicio.dto.ProcesarPagoRequest;
import com.foodfast.pago_servicio.exception.RecursoNoEncontradoException;
import com.foodfast.pago_servicio.exception.ReglaNegocioException;
import com.foodfast.pago_servicio.model.EstadoPago;
import com.foodfast.pago_servicio.model.Pago;
import com.foodfast.pago_servicio.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
 
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
 
@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {
 
    private final PagoRepository pagoRepository;
 
    public List<PagoResponse> listar() {
        log.info("Listando pagos");
        return pagoRepository.findAll().stream().map(this::toResponse).toList();
    }
 
    public PagoResponse buscarPorId(Long id) {
        log.info("Buscando pago id={}", id);
        return toResponse(obtenerEntidad(id));
    }
 
    public List<PagoResponse> buscarPorPedidoId(Long pedidoId) {
        log.info("Buscando pagos por pedidoId={}", pedidoId);
        return pagoRepository.findByPedidoId(pedidoId).stream().map(this::toResponse).toList();
    }
 
    public PagoResponse crear(PagoRequest request) {
        log.info("Creando pago pendiente para pedidoId={}", request.getPedidoId());
        if (pagoRepository.existsByPedidoId(request.getPedidoId())) {
            throw new ReglaNegocioException("Ya existe un pago registrado para este pedido");
        }
        Pago pago = Pago.builder()
                .pedidoId(request.getPedidoId())
                .monto(request.getMonto())
                .metodoPago(request.getMetodoPago())
                .estadoPago(EstadoPago.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();
        return toResponse(pagoRepository.save(pago));
    }
 
    public PagoResponse procesar(ProcesarPagoRequest request) {
        log.info("Procesando pago id={} aprobado={}", request.getPagoId(), request.getAprobado());
        Pago pago = obtenerEntidad(request.getPagoId());
        if (pago.getEstadoPago() != EstadoPago.PENDIENTE) {
            throw new ReglaNegocioException("Solo se pueden procesar pagos pendientes");
        }
        pago.setEstadoPago(Boolean.TRUE.equals(request.getAprobado()) ? EstadoPago.APROBADO : EstadoPago.RECHAZADO);
        pago.setCodigoTransaccion("FF-" + UUID.randomUUID());
        pago.setFechaProcesamiento(LocalDateTime.now());
        return toResponse(pagoRepository.save(pago));
    }
 
    public PagoResponse anular(Long id) {
        log.warn("Anulando pago id={}", id);
        Pago pago = obtenerEntidad(id);
        if (pago.getEstadoPago() == EstadoPago.APROBADO) {
            throw new ReglaNegocioException("No se puede anular un pago aprobado desde este flujo básico");
        }
        pago.setEstadoPago(EstadoPago.ANULADO);
        return toResponse(pagoRepository.save(pago));
    }
 
    public void eliminar(Long id) {
        log.warn("Eliminando pago id={}", id);
        pagoRepository.delete(obtenerEntidad(id));
    }
    private Pago obtenerEntidad(Long id) {
        return pagoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado con id: " + id));
    }
    private PagoResponse toResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .pedidoId(pago.getPedidoId())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .estadoPago(pago.getEstadoPago())
                .codigoTransaccion(pago.getCodigoTransaccion())
                .fechaCreacion(pago.getFechaCreacion())
                .fechaProcesamiento(pago.getFechaProcesamiento())
                .build();
    }
}