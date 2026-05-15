package com.foodfast.pago_servicio.controller;
 
import com.foodfast.pago_servicio.dto.ApiResponse;
import com.foodfast.pago_servicio.dto.PagoRequest;
import com.foodfast.pago_servicio.dto.PagoResponse;
import com.foodfast.pago_servicio.dto.ProcesarPagoRequest;
import com.foodfast.pago_servicio.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {
 
    private final PagoService pagoService;
 
    @GetMapping
    public ResponseEntity<ApiResponse<List<PagoResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok("Pagos listados", pagoService.listar()));
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PagoResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Pago encontrado", pagoService.buscarPorId(id)));
    }
 
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<ApiResponse<List<PagoResponse>>> buscarPorPedidoId(@PathVariable Long pedidoId) {
        return ResponseEntity.ok(ApiResponse.ok("Pagos del pedido", pagoService.buscarPorPedidoId(pedidoId)));
    }
 
    @PostMapping
    public ResponseEntity<ApiResponse<PagoResponse>> crear(@Valid @RequestBody PagoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Pago creado en estado pendiente", pagoService.crear(request)));
    }
 
    @PostMapping("/procesar")
    public ResponseEntity<ApiResponse<PagoResponse>> procesar(@Valid @RequestBody ProcesarPagoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Pago procesado", pagoService.procesar(request)));
    }
 
    @PatchMapping("/{id}/anular")
    public ResponseEntity<ApiResponse<PagoResponse>> anular(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Pago anulado", pagoService.anular(id)));
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
