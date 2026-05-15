package com.foodfast.inventario_servicio.service;

import com.foodfast.inventario_servicio.dto.DescontarStockRequest;
import com.foodfast.inventario_servicio.dto.InventarioRequest;
import com.foodfast.inventario_servicio.dto.InventarioResponse;
import com.foodfast.inventario_servicio.dto.StockResponse;
import com.foodfast.inventario_servicio.exception.RecursoNoEncontradoException;
import com.foodfast.inventario_servicio.exception.ReglaNegocioException;
import com.foodfast.inventario_servicio.exception.StockInsuficienteException;
import com.foodfast.inventario_servicio.model.Inventario;
import com.foodfast.inventario_servicio.repository.InventarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public List<InventarioResponse> listar() {
        log.info("Listando inventarios");
        return inventarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public InventarioResponse buscarPorId(Long id) {
        log.info("Buscando inventario id={}", id);
        return toResponse(obtenerEntidad(id));
    }

    public InventarioResponse buscarPorProductoId(Long productoId) {
        log.info("Buscando inventario productoId={}", productoId);
        Inventario inventario = obtenerPorProductoId(productoId);
        return toResponse(inventario);
    }

    public StockResponse consultarStock(Long productoId) {
        log.info("Consultando stock productoId={}", productoId);
        Inventario inventario = obtenerPorProductoId(productoId);
        Integer stock = calcularStockReal(inventario);
        return new StockResponse(productoId, stock, stock > 0);
    }

    public InventarioResponse crear(InventarioRequest request) {
        validarStockNoNegativo(request.getCantidadDisponible(), request.getCantidadReservada());

        if (inventarioRepository.existsByProductoId(request.getProductoId())) {
            throw new ReglaNegocioException("Ya existe inventario para el productoId: " + request.getProductoId());
        }

        Inventario inventario = new Inventario();
        copiarDatos(request, inventario);

        Inventario guardado = inventarioRepository.save(inventario);
        log.info("Inventario creado id={} productoId={} stockReal={}", guardado.getId(), guardado.getProductoId(), calcularStockReal(guardado));
        return toResponse(guardado);
    }

    public InventarioResponse actualizar(Long id, InventarioRequest request) {
        validarStockNoNegativo(request.getCantidadDisponible(), request.getCantidadReservada());

        Inventario inventario = obtenerEntidad(id);

        inventarioRepository.findByProductoId(request.getProductoId()).ifPresent(encontrado -> {
            if (!encontrado.getId().equals(id)) {
                throw new ReglaNegocioException("Otro registro ya usa el productoId: " + request.getProductoId());
            }
        });

        copiarDatos(request, inventario);

        Inventario actualizado = inventarioRepository.save(inventario);
        log.info("Inventario actualizado id={} productoId={} stockReal={}", actualizado.getId(), actualizado.getProductoId(), calcularStockReal(actualizado));
        return toResponse(actualizado);
    }

    @Transactional
    public InventarioResponse descontarStock(DescontarStockRequest request) {
        Inventario inventario = obtenerPorProductoId(request.getProductoId());
        Integer stockActual = calcularStockReal(inventario);

        if (stockActual < request.getCantidad()) {
            log.warn("Stock insuficiente productoId={} disponible={} solicitado={}", request.getProductoId(), stockActual, request.getCantidad());
            throw new StockInsuficienteException(
                    "Stock insuficiente. Disponible: " + stockActual + ", solicitado: " + request.getCantidad()
            );
        }

        inventario.setCantidadDisponible(inventario.getCantidadDisponible() - request.getCantidad());
        validarStockNoNegativo(inventario.getCantidadDisponible(), inventario.getCantidadReservada());

        Inventario actualizado = inventarioRepository.save(inventario);
        log.info("Stock descontado productoId={} cantidad={} stockRealFinal={}", request.getProductoId(), request.getCantidad(), calcularStockReal(actualizado));
        return toResponse(actualizado);
    }

    public void eliminar(Long id) {
        Inventario inventario = obtenerEntidad(id);
        inventarioRepository.delete(inventario);
        log.info("Inventario eliminado id={}", id);
    }

    private Inventario obtenerEntidad(Long id) {
        validarId(id, "id");
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inventario no encontrado con id: " + id));
    }

    private Inventario obtenerPorProductoId(Long productoId) {
        validarId(productoId, "productoId");
        return inventarioRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe inventario para productoId: " + productoId));
    }

    private void copiarDatos(InventarioRequest request, Inventario inventario) {
        inventario.setProductoId(request.getProductoId());
        inventario.setCantidadDisponible(request.getCantidadDisponible());
        inventario.setCantidadReservada(request.getCantidadReservada());
        inventario.setUbicacion(request.getUbicacion());
    }

    private void validarId(Long id, String campo) {
        if (id == null || id <= 0) {
            throw new ReglaNegocioException("El " + campo + " debe ser mayor que cero");
        }
    }

    private void validarStockNoNegativo(Integer disponible, Integer reservado) {
        if (disponible == null || reservado == null) {
            throw new ReglaNegocioException("La cantidad disponible y la cantidad reservada son obligatorias");
        }
        if (disponible < 0 || reservado < 0) {
            throw new ReglaNegocioException("El stock no puede ser negativo");
        }
        if (reservado > disponible) {
            throw new ReglaNegocioException("La cantidad reservada no puede superar la cantidad disponible");
        }
    }

    private Integer calcularStockReal(Inventario inventario) {
        return inventario.getCantidadDisponible() - inventario.getCantidadReservada();
    }

    private InventarioResponse toResponse(Inventario inventario) {
        return new InventarioResponse(
                inventario.getId(),
                inventario.getProductoId(),
                inventario.getCantidadDisponible(),
                inventario.getCantidadReservada(),
                calcularStockReal(inventario),
                inventario.getUbicacion()
        );
    }
}
