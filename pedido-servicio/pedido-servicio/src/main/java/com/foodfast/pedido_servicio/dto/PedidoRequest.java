package com.foodfast.pedido_servicio.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PedidoRequest {

    private Long clienteId;

    private BigDecimal total;

    private String direccionEntrega;

    private String observacion;
}