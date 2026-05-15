package com.foodfast.resena_servicio.controller;

import com.foodfast.resena_servicio.dto.ApiResponse;
import com.foodfast.resena_servicio.dto.PromedioResenaResponse;
import com.foodfast.resena_servicio.dto.ResenaRequest;
import com.foodfast.resena_servicio.dto.ResenaResponse;
import com.foodfast.resena_servicio.service.ResenaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResenaResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<ResenaResponse>>builder()
                .success(true)
                .message("Reseñas listadas correctamente")
                .data(resenaService.listar())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResenaResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ResenaResponse>builder()
                .success(true)
                .message("Reseña encontrada")
                .data(resenaService.buscarPorId(id))
                .build());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<ApiResponse<List<ResenaResponse>>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(ApiResponse.<List<ResenaResponse>>builder()
                .success(true)
                .message("Reseñas por producto listadas correctamente")
                .data(resenaService.listarPorProducto(productoId))
                .build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<ResenaResponse>>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ApiResponse.<List<ResenaResponse>>builder()
                .success(true)
                .message("Reseñas por cliente listadas correctamente")
                .data(resenaService.listarPorCliente(clienteId))
                .build());
    }

    @GetMapping("/producto/{productoId}/promedio")
    public ResponseEntity<ApiResponse<PromedioResenaResponse>> promedioPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(ApiResponse.<PromedioResenaResponse>builder()
                .success(true)
                .message("Promedio calculado correctamente")
                .data(resenaService.promedioPorProducto(productoId))
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ResenaResponse>> crear(@Valid @RequestBody ResenaRequest request) {
        ResenaResponse creada = resenaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ResenaResponse>builder()
                .success(true)
                .message("Reseña creada correctamente")
                .data(creada)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResenaResponse>> actualizar(@PathVariable Long id, @Valid @RequestBody ResenaRequest request) {
        return ResponseEntity.ok(ApiResponse.<ResenaResponse>builder()
                .success(true)
                .message("Reseña actualizada correctamente")
                .data(resenaService.actualizar(id, request))
                .build());
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<ResenaResponse>> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ResenaResponse>builder()
                .success(true)
                .message("Reseña desactivada correctamente")
                .data(resenaService.desactivar(id))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
