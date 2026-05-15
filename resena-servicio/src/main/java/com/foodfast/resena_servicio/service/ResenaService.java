package com.foodfast.resena_servicio.service;

import com.foodfast.resena_servicio.dto.PromedioResenaResponse;
import com.foodfast.resena_servicio.dto.ResenaRequest;
import com.foodfast.resena_servicio.dto.ResenaResponse;
import com.foodfast.resena_servicio.exception.RecursoNoEncontradoException;
import com.foodfast.resena_servicio.exception.ReglaNegocioException;
import com.foodfast.resena_servicio.model.Resena;
import com.foodfast.resena_servicio.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private static final Logger log = LoggerFactory.getLogger(ResenaService.class);
    private final ResenaRepository resenaRepository;

    public List<ResenaResponse> listar() {
        log.info("Listando reseñas");
        return resenaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public ResenaResponse buscarPorId(Long id) {
        log.info("Buscando reseña id={}", id);
        return toResponse(obtenerResena(id));
    }

    public List<ResenaResponse> listarPorProducto(Long productoId) {
        log.info("Listando reseñas por productoId={}", productoId);
        return resenaRepository.findByProductoId(productoId).stream().map(this::toResponse).toList();
    }

    public List<ResenaResponse> listarPorCliente(Long clienteId) {
        log.info("Listando reseñas por clienteId={}", clienteId);
        return resenaRepository.findByClienteId(clienteId).stream().map(this::toResponse).toList();
    }

    public PromedioResenaResponse promedioPorProducto(Long productoId) {
        log.info("Calculando promedio de reseñas para productoId={}", productoId);
        Double promedio = resenaRepository.promedioPorProducto(productoId);
        Long total = resenaRepository.countByProductoIdAndActivaTrue(productoId);
        return PromedioResenaResponse.builder()
                .productoId(productoId)
                .promedio(promedio == null ? 0.0 : promedio)
                .totalResenas(total)
                .build();
    }

    @Transactional
    public ResenaResponse crear(ResenaRequest request) {
        log.info("Creando reseña clienteId={} productoId={}", request.getClienteId(), request.getProductoId());
        if (resenaRepository.existsByClienteIdAndProductoId(request.getClienteId(), request.getProductoId())) {
            throw new ReglaNegocioException("El cliente ya registro una reseña para este producto");
        }

        Resena resena = Resena.builder()
                .clienteId(request.getClienteId())
                .productoId(request.getProductoId())
                .restauranteId(request.getRestauranteId())
                .calificacion(request.getCalificacion())
                .comentario(request.getComentario())
                .activa(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        Resena guardada = resenaRepository.save(resena);
        log.info("Reseña creada id={}", guardada.getId());
        return toResponse(guardada);
    }

    @Transactional
    public ResenaResponse actualizar(Long id, ResenaRequest request) {
        Resena resena = obtenerResena(id);
        log.info("Actualizando reseña id={}", id);

        resenaRepository.findByClienteIdAndProductoId(request.getClienteId(), request.getProductoId())
                .ifPresent(encontrada -> {
                    if (!encontrada.getId().equals(id)) {
                        throw new ReglaNegocioException("Otro registro ya usa el mismo clienteId y productoId");
                    }
                });

        resena.setClienteId(request.getClienteId());
        resena.setProductoId(request.getProductoId());
        resena.setRestauranteId(request.getRestauranteId());
        resena.setCalificacion(request.getCalificacion());
        resena.setComentario(request.getComentario());
        resena.setFechaActualizacion(LocalDateTime.now());

        return toResponse(resenaRepository.save(resena));
    }

    @Transactional
    public ResenaResponse desactivar(Long id) {
        Resena resena = obtenerResena(id);
        resena.setActiva(false);
        resena.setFechaActualizacion(LocalDateTime.now());
        log.info("Reseña id={} desactivada", id);
        return toResponse(resenaRepository.save(resena));
    }

    @Transactional
    public void eliminar(Long id) {
        Resena resena = obtenerResena(id);
        log.info("Eliminando reseña id={}", id);
        resenaRepository.delete(resena);
    }

    private Resena obtenerResena(Long id) {
        return resenaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reseña no encontrada con id: " + id));
    }

    private ResenaResponse toResponse(Resena resena) {
        return ResenaResponse.builder()
                .id(resena.getId())
                .clienteId(resena.getClienteId())
                .productoId(resena.getProductoId())
                .restauranteId(resena.getRestauranteId())
                .calificacion(resena.getCalificacion())
                .comentario(resena.getComentario())
                .activa(resena.getActiva())
                .fechaCreacion(resena.getFechaCreacion())
                .fechaActualizacion(resena.getFechaActualizacion())
                .build();
    }
}
