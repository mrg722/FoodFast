package com.foodfast.pago_servicio.dto;
 
import com.foodfast.pago_servicio.model.MetodoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
 
import java.math.BigDecimal;
 
@Data
public class PagoRequest {
 
    @NotNull(message = "El pedidoId es obligatorio")
    private Long pedidoId;
 
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "1.0", message = "El monto debe ser mayor que cero")
    private BigDecimal monto;
 
    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;
}
