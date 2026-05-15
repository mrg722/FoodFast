package com.foodfast.resena_servicio.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromedioResenaResponse {
    private Long productoId;
    private Double promedio;
    private Long totalResenas;
}
