package com.foodfast.inventario_servicio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventarioRequest {

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad disponible es obligatoria")
    @Min(value = 0, message = "La cantidad disponible no puede ser negativa")
    private Integer cantidadDisponible;

    @NotNull(message = "La cantidad reservada es obligatoria")
    @Min(value = 0, message = "La cantidad reservada no puede ser negativa")
    private Integer cantidadReservada;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 120, message = "La ubicación no puede superar 120 caracteres")
    private String ubicacion;
}
