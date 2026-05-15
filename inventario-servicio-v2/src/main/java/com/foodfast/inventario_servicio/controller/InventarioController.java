package com.foodfast.inventario_servicio.controller;

import com.foodfast.inventario_servicio.dto.ApiResponse;
import com.foodfast.inventario_servicio.dto.DescontarStockRequest;
import com.foodfast.inventario_servicio.dto.InventarioRequest;
import com.foodfast.inventario_servicio.dto.InventarioResponse;
import com.foodfast.inventario_servicio.dto.StockResponse;
import com.foodfast.inventario_servicio.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventarioResponse>>> listar() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Inventarios listados correctamente", inventarioService.listar()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventarioResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Inventario encontrado", inventarioService.buscarPorId(id)));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<ApiResponse<InventarioResponse>> buscarPorProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Inventario encontrado por productoId", inventarioService.buscarPorProductoId(productoId)));
    }

    @GetMapping("/producto/{productoId}/stock")
    public ResponseEntity<ApiResponse<StockResponse>> consultarStock(@PathVariable Long productoId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Stock consultado correctamente", inventarioService.consultarStock(productoId)));
    }

    @GetMapping("/stock/{productoId}")
    public ResponseEntity<ApiResponse<StockResponse>> consultarStockAlias(@PathVariable Long productoId) {
        return consultarStock(productoId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventarioResponse>> crear(@Valid @RequestBody InventarioRequest request) {
        InventarioResponse inventario = inventarioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Inventario creado correctamente", inventario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventarioResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Inventario actualizado correctamente", inventarioService.actualizar(id, request)));
    }

    @PutMapping("/descontar-stock")
    public ResponseEntity<ApiResponse<InventarioResponse>> descontarStock(
            @Valid @RequestBody DescontarStockRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Stock descontado correctamente", inventarioService.descontarStock(request)));
    }

    @PostMapping("/descontar")
    public ResponseEntity<ApiResponse<InventarioResponse>> descontarStockAlias(
            @Valid @RequestBody DescontarStockRequest request
    ) {
        return descontarStock(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
