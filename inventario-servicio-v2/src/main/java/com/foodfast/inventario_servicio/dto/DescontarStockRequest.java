package com.foodfast.inventario_servicio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DescontarStockRequest {

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad a descontar es obligatoria")
    @Min(value = 1, message = "La cantidad a descontar debe ser mayor que cero")
    private Integer cantidad;
}
