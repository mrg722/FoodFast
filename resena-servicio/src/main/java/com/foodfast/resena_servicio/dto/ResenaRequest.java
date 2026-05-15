package com.foodfast.resena_servicio.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenaRequest {

    @NotNull(message = "El clienteId es obligatorio")
    @Positive(message = "El clienteId debe ser mayor que cero")
    private Long clienteId;

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor que cero")
    private Long productoId;

    @Positive(message = "El restauranteId debe ser mayor que cero")
    private Long restauranteId;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(max = 500, message = "El comentario no puede superar 500 caracteres")
    private String comentario;
}
