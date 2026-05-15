package com.foodfast.autenticacion_servicio.controller;

import com.foodfast.autenticacion_servicio.dto.ApiResponse;
import com.foodfast.autenticacion_servicio.dto.AuthResponse;
import com.foodfast.autenticacion_servicio.dto.LoginRequest;
import com.foodfast.autenticacion_servicio.dto.RegistroRequest;
import com.foodfast.autenticacion_servicio.dto.UsuarioResponse;
import com.foodfast.autenticacion_servicio.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registrar usuario y devolver token JWT")
    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<AuthResponse>> registrar(@Valid @RequestBody RegistroRequest request) {
        AuthResponse response = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Usuario registrado correctamente")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Login de usuario y devolucion de token JWT")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .success(true)
                        .message("Login correcto")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Obtener perfil del usuario autenticado")
    @GetMapping("/perfil")
    public ResponseEntity<ApiResponse<UsuarioResponse>> perfil(Principal principal) {
        UsuarioResponse response = authService.obtenerPerfil(principal.getName());
        return ResponseEntity.ok(
                ApiResponse.<UsuarioResponse>builder()
                        .success(true)
                        .message("Perfil obtenido correctamente")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Listar usuarios registrados. Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public ResponseEntity<ApiResponse<List<UsuarioResponse>>> listarUsuarios() {
        List<UsuarioResponse> response = authService.listarUsuarios();
        return ResponseEntity.ok(
                ApiResponse.<List<UsuarioResponse>>builder()
                        .success(true)
                        .message("Usuarios listados correctamente")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Buscar usuario por id. Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> buscarUsuario(@PathVariable Long id) {
        UsuarioResponse response = authService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(
                ApiResponse.<UsuarioResponse>builder()
                        .success(true)
                        .message("Usuario encontrado correctamente")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Desactivar usuario. Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usuarios/{id}/desactivar")
    public ResponseEntity<ApiResponse<UsuarioResponse>> desactivarUsuario(@PathVariable Long id) {
        UsuarioResponse response = authService.cambiarEstado(id, false);
        return ResponseEntity.ok(
                ApiResponse.<UsuarioResponse>builder()
                        .success(true)
                        .message("Usuario desactivado correctamente")
                        .data(response)
                        .build()
        );
    }

    @Operation(summary = "Activar usuario. Requiere rol ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usuarios/{id}/activar")
    public ResponseEntity<ApiResponse<UsuarioResponse>> activarUsuario(@PathVariable Long id) {
        UsuarioResponse response = authService.cambiarEstado(id, true);
        return ResponseEntity.ok(
                ApiResponse.<UsuarioResponse>builder()
                        .success(true)
                        .message("Usuario activado correctamente")
                        .data(response)
                        .build()
        );
    }
}
