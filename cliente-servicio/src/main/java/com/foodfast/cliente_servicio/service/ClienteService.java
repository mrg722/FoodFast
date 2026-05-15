package com.foodfast.cliente_servicio.service;

import com.foodfast.cliente_servicio.dto.*;
import com.foodfast.cliente_servicio.exception.RecursoNoEncontradoException;
import com.foodfast.cliente_servicio.exception.ReglaNegocioException;
import com.foodfast.cliente_servicio.model.Cliente;
import com.foodfast.cliente_servicio.model.Direccion;
import com.foodfast.cliente_servicio.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        log.info("Listando clientes");
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        log.info("Buscando cliente id={}", id);
        return toResponse(obtenerCliente(id));
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorEmail(String email) {
        log.info("Buscando cliente por email={}", email);
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con email: " + email));
        return toResponse(cliente);
    }

    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        log.info("Creando cliente con email={}", request.getEmail());

        if (clienteRepository.existsByEmail(request.getEmail())) {
            throw new ReglaNegocioException("Ya existe un cliente con el email: " + request.getEmail());
        }

        Cliente cliente = Cliente.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .activo(true)
                .build();

        if (request.getDirecciones() != null) {
            request.getDirecciones()
                    .forEach(direccionRequest -> cliente.agregarDireccion(toDireccion(direccionRequest)));
        }

        validarDireccionPrincipal(cliente);
        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente creado id={}", guardado.getId());
        return toResponse(guardado);
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = obtenerCliente(id);
        log.info("Actualizando cliente id={}", id);

        clienteRepository.findByEmail(request.getEmail()).ifPresent(encontrado -> {
            if (!encontrado.getId().equals(id)) {
                throw new ReglaNegocioException("Otro cliente ya usa el email: " + request.getEmail());
            }
        });

        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());

        cliente.limpiarDirecciones();
        if (request.getDirecciones() != null) {
            request.getDirecciones()
                    .forEach(direccionRequest -> cliente.agregarDireccion(toDireccion(direccionRequest)));
        }

        validarDireccionPrincipal(cliente);
        Cliente actualizado = clienteRepository.save(cliente);
        log.info("Cliente actualizado id={}", actualizado.getId());
        return toResponse(actualizado);
    }

    @Transactional
    public ClienteResponse desactivar(Long id) {
        Cliente cliente = obtenerCliente(id);
        cliente.setActivo(false);
        Cliente actualizado = clienteRepository.save(cliente);
        log.info("Cliente id={} desactivado", id);
        return toResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = obtenerCliente(id);
        log.info("Eliminando cliente id={}", id);
        clienteRepository.delete(cliente);
    }

    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id: " + id));
    }

    private Direccion toDireccion(DireccionRequest request) {
        return Direccion.builder()
                .calle(request.getCalle())
                .numero(request.getNumero())
                .comuna(request.getComuna())
                .ciudad(request.getCiudad())
                .referencia(request.getReferencia())
                .principal(request.getPrincipal())
                .build();
    }

    private void validarDireccionPrincipal(Cliente cliente) {
        long principales = cliente.getDirecciones()
                .stream()
                .filter(direccion -> Boolean.TRUE.equals(direccion.getPrincipal()))
                .count();

        if (!cliente.getDirecciones().isEmpty() && principales != 1) {
            throw new ReglaNegocioException("El cliente debe tener exactamente una direccion principal");
        }
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .activo(cliente.getActivo())
                .direcciones(cliente.getDirecciones().stream().map(this::toDireccionResponse).toList())
                .build();
    }

    private DireccionResponse toDireccionResponse(Direccion direccion) {
        return DireccionResponse.builder()
                .id(direccion.getId())
                .calle(direccion.getCalle())
                .numero(direccion.getNumero())
                .comuna(direccion.getComuna())
                .ciudad(direccion.getCiudad())
                .referencia(direccion.getReferencia())
                .principal(direccion.getPrincipal())
                .build();
    }
}
