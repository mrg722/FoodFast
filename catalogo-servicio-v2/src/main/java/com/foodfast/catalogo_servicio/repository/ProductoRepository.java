package com.foodfast.catalogo_servicio.repository;

import com.foodfast.catalogo_servicio.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
}
