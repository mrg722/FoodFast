package com.foodfast.catalogo_servicio.service;

import com.foodfast.catalogo_servicio.dto.CategoriaRequest;
import com.foodfast.catalogo_servicio.dto.CategoriaResponse;
import com.foodfast.catalogo_servicio.exception.RecursoNoEncontradoException;
import com.foodfast.catalogo_servicio.exception.ReglaNegocioException;
import com.foodfast.catalogo_servicio.model.Categoria;
import com.foodfast.catalogo_servicio.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponse> listar() {
        log.info("Listando categorías");
        return categoriaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoriaResponse buscarPorId(Long id) {
        log.info("Buscando categoría id={}", id);
        return toResponse(obtenerEntidad(id));
    }

    public CategoriaResponse crear(CategoriaRequest request) {
        validarNombreDuplicado(request.getNombre());

        Categoria categoria = new Categoria();
        copiarDatos(request, categoria);

        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoría creada id={} nombre={}", guardada.getId(), guardada.getNombre());
        return toResponse(guardada);
    }

    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = obtenerEntidad(id);

        if (!categoria.getNombre().equalsIgnoreCase(request.getNombre())) {
            validarNombreDuplicado(request.getNombre());
        }

        copiarDatos(request, categoria);
        Categoria actualizada = categoriaRepository.save(categoria);
        log.info("Categoría actualizada id={}", actualizada.getId());
        return toResponse(actualizada);
    }

    public void eliminar(Long id) {
        Categoria categoria = obtenerEntidad(id);
        categoriaRepository.delete(categoria);
        log.info("Categoría eliminada id={}", id);
    }

    public Categoria obtenerEntidad(Long id) {
        if (id == null || id <= 0) {
            throw new ReglaNegocioException("El id de categoría debe ser mayor que cero");
        }
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id: " + id));
    }

    private void validarNombreDuplicado(String nombre) {
        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe una categoría con el nombre: " + nombre);
        }
    }

    private void copiarDatos(CategoriaRequest request, Categoria categoria) {
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());
        categoria.setActiva(request.getActiva());
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getActiva()
        );
    }
}
