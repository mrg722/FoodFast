package com.foodfast.pedido_servicio.service;

import com.foodfast.pedido_servicio.dto.PedidoRequest;
import com.foodfast.pedido_servicio.model.EstadoPedido;
import com.foodfast.pedido_servicio.model.Pedido;
import com.foodfast.pedido_servicio.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public Pedido crear(PedidoRequest request) {

        Pedido pedido = Pedido.builder()
                .clienteId(request.getClienteId())
                .fechaCreacion(LocalDateTime.now())
                .estado(EstadoPedido.CONFIRMADO)
                .total(request.getTotal())
                .direccionEntrega(request.getDireccionEntrega())
                .observacion(request.getObservacion())
                .build();

        return pedidoRepository.save(pedido);
    }
}