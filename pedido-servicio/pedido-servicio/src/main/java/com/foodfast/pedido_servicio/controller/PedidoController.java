package com.foodfast.pedido_servicio.controller;

import com.foodfast.pedido_servicio.dto.PedidoRequest;
import com.foodfast.pedido_servicio.model.Pedido;
import com.foodfast.pedido_servicio.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor

public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public List<Pedido> listar() {
        return pedidoService.listar();
    }

    @PostMapping
    public Pedido crear(@RequestBody PedidoRequest request) {
        return pedidoService.crear(request);
    }
}
