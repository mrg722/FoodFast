package com.foodfast.pedido_servicio.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;

    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    private BigDecimal total;

    private String direccionEntrega;

    private String observacion;
}