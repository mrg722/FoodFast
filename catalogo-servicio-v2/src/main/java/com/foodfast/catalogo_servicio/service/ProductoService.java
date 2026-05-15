package com.foodfast.catalogo_servicio.service;

import com.foodfast.catalogo_servicio.dto.ProductoRequest;
import com.foodfast.catalogo_servicio.dto.ProductoResponse;
import com.foodfast.catalogo_servicio.exception.RecursoNoEncontradoException;
import com.foodfast.catalogo_servicio.exception.ReglaNegocioException;
import com.foodfast.catalogo_servicio.model.Categoria;
import com.foodfast.catalogo_servicio.model.Producto;
import com.foodfast.catalogo_servicio.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;

    public List<ProductoResponse> listar() {
        log.info("Listando productos");
        return productoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductoResponse buscarPorId(Long id) {
        log.info("Buscando producto id={}", id);
        return toResponse(obtenerEntidad(id));
    }

    public ProductoResponse crear(ProductoRequest request) {
        validarNombreDuplicado(request.getNombre());

        Categoria categoria = categoriaService.obtenerEntidad(request.getCategoriaId());
        validarCategoriaActiva(categoria);

        Producto producto = new Producto();
        copiarDatos(request, producto, categoria);

        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado id={} nombre={} categoriaId={}", guardado.getId(), guardado.getNombre(), categoria.getId());
        return toResponse(guardado);
    }

    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = obtenerEntidad(id);

        if (!producto.getNombre().equalsIgnoreCase(request.getNombre())) {
            validarNombreDuplicado(request.getNombre());
        }

        Categoria categoria = categoriaService.obtenerEntidad(request.getCategoriaId());
        validarCategoriaActiva(categoria);

        copiarDatos(request, producto, categoria);
        Producto actualizado = productoRepository.save(producto);
        log.info("Producto actualizado id={}", actualizado.getId());
        return toResponse(actualizado);
    }

    public void eliminar(Long id) {
        Producto producto = obtenerEntidad(id);
        productoRepository.delete(producto);
        log.info("Producto eliminado id={}", id);
    }

    private Producto obtenerEntidad(Long id) {
        if (id == null || id <= 0) {
            throw new ReglaNegocioException("El id de producto debe ser mayor que cero");
        }
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    private void validarNombreDuplicado(String nombre) {
        if (productoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe un producto con el nombre: " + nombre);
        }
    }

    private void validarCategoriaActiva(Categoria categoria) {
        if (Boolean.FALSE.equals(categoria.getActiva())) {
            throw new ReglaNegocioException("No se puede asociar un producto a una categoría inactiva");
        }
    }

    private void copiarDatos(ProductoRequest request, Producto producto, Categoria categoria) {
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setDisponible(request.getDisponible());
        producto.setCategoria(categoria);
    }

    private ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getDisponible(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre()
        );
    }
}
