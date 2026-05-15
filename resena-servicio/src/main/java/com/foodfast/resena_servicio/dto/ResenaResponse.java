package com.foodfast.resena_servicio.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenaResponse {
    private Long id;
    private Long clienteId;
    private Long productoId;
    private Long restauranteId;
    private Integer calificacion;
    private String comentario;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
