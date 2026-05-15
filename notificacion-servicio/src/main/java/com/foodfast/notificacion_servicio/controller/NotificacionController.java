package com.foodfast.notificacion_servicio.controller;

import com.foodfast.notificacion_servicio.dto.ApiResponse;
import com.foodfast.notificacion_servicio.dto.NotificacionRequest;
import com.foodfast.notificacion_servicio.dto.NotificacionResponse;
import com.foodfast.notificacion_servicio.model.EstadoNotificacion;
import com.foodfast.notificacion_servicio.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificacionResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<NotificacionResponse>>builder()
                .success(true)
                .message("Notificaciones listadas correctamente")
                .data(notificacionService.listar())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificacionResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<NotificacionResponse>builder()
                .success(true)
                .message("Notificacion encontrada")
                .data(notificacionService.buscarPorId(id))
                .build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<NotificacionResponse>>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ApiResponse.<List<NotificacionResponse>>builder()
                .success(true)
                .message("Notificaciones por cliente listadas correctamente")
                .data(notificacionService.listarPorCliente(clienteId))
                .build());
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<NotificacionResponse>>> listarPorEstado(@PathVariable EstadoNotificacion estado) {
        return ResponseEntity.ok(ApiResponse.<List<NotificacionResponse>>builder()
                .success(true)
                .message("Notificaciones por estado listadas correctamente")
                .data(notificacionService.listarPorEstado(estado))
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificacionResponse>> crear(@Valid @RequestBody NotificacionRequest request) {
        NotificacionResponse creada = notificacionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<NotificacionResponse>builder()
                .success(true)
                .message("Notificacion creada correctamente")
                .data(creada)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificacionResponse>> actualizar(@PathVariable Long id, @Valid @RequestBody NotificacionRequest request) {
        return ResponseEntity.ok(ApiResponse.<NotificacionResponse>builder()
                .success(true)
                .message("Notificacion actualizada correctamente")
                .data(notificacionService.actualizar(id, request))
                .build());
    }

    @PutMapping("/{id}/enviar")
    public ResponseEntity<ApiResponse<NotificacionResponse>> enviar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<NotificacionResponse>builder()
                .success(true)
                .message("Notificacion enviada correctamente")
                .data(notificacionService.enviar(id))
                .build());
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<ApiResponse<NotificacionResponse>> marcarComoLeida(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<NotificacionResponse>builder()
                .success(true)
                .message("Notificacion marcada como leida")
                .data(notificacionService.marcarComoLeida(id))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
