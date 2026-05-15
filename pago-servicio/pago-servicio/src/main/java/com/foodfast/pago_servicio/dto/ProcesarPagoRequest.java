package com.foodfast.pago_servicio.dto;
 
import jakarta.validation.constraints.NotNull;
import lombok.Data;
 
@Data
public class ProcesarPagoRequest {
 
    @NotNull(message = "El pagoId es obligatorio")
    private Long pagoId;
 
    @NotNull(message = "Debe indicar si el pago fue aprobado")
    private Boolean aprobado;
}
