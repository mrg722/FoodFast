package com.foodfast.cliente_servicio.controller;

import com.foodfast.cliente_servicio.dto.ApiResponse;
import com.foodfast.cliente_servicio.dto.ClienteRequest;
import com.foodfast.cliente_servicio.dto.ClienteResponse;
import com.foodfast.cliente_servicio.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.<List<ClienteResponse>>builder()
                .success(true)
                .message("Clientes listados correctamente")
                .data(clienteService.listar())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ClienteResponse>builder()
                .success(true)
                .message("Cliente encontrado")
                .data(clienteService.buscarPorId(id))
                .build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<ClienteResponse>> buscarPorEmail(@PathVariable String email) {
        return ResponseEntity.ok(ApiResponse.<ClienteResponse>builder()
                .success(true)
                .message("Cliente encontrado por email")
                .data(clienteService.buscarPorEmail(email))
                .build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> crear(@Valid @RequestBody ClienteRequest request) {
        ClienteResponse creado = clienteService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ClienteResponse>builder()
                .success(true)
                .message("Cliente creado correctamente")
                .data(creado)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.<ClienteResponse>builder()
                .success(true)
                .message("Cliente actualizado correctamente")
                .data(clienteService.actualizar(id, request))
                .build());
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<ClienteResponse>> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<ClienteResponse>builder()
                .success(true)
                .message("Cliente desactivado correctamente")
                .data(clienteService.desactivar(id))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
