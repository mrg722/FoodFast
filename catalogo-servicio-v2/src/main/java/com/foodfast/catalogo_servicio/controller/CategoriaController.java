package com.foodfast.catalogo_servicio.controller;

import com.foodfast.catalogo_servicio.dto.ApiResponse;
import com.foodfast.catalogo_servicio.dto.CategoriaRequest;
import com.foodfast.catalogo_servicio.dto.CategoriaResponse;
import com.foodfast.catalogo_servicio.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaResponse>>> listar() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Categorías listadas correctamente", categoriaService.listar()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponse>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Categoría encontrada", categoriaService.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaResponse>> crear(@Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse categoria = categoriaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Categoría creada correctamente", categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Categoría actualizada correctamente", categoriaService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
