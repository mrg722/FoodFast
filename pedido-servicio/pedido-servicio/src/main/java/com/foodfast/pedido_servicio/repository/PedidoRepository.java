package com.foodfast.pedido_servicio.repository;

import com.foodfast.pedido_servicio.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}