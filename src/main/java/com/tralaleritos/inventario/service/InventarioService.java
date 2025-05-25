package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.dto.InventarioRequestDTO;
import com.tralaleritos.inventario.dto.InventarioResponseDTO;
import com.tralaleritos.inventario.model.Inventario;
import com.tralaleritos.inventario.model.Producto;
import com.tralaleritos.inventario.model.Tienda;
import com.tralaleritos.inventario.repository.InventarioRepository;
import com.tralaleritos.inventario.repository.ProductoRepository;
import com.tralaleritos.inventario.repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TiendaRepository tiendaRepository;

    private InventarioResponseDTO convertirAResponseDto(Inventario inventario) {
        InventarioResponseDTO dto = new InventarioResponseDTO();
        dto.setId(inventario.getId());
        dto.setCantidadDisponible(inventario.getCantidadDisponible());
        dto.setPuntoReorden(inventario.getPuntoReorden());
        if (inventario.getProducto() != null) {
            dto.setProductoId(inventario.getProducto().getId());
            dto.setNombreProducto(inventario.getProducto().getNombre());
        }
        if (inventario.getTienda() != null) {
            dto.setTiendaId(inventario.getTienda().getId());
            dto.setNombreTienda(inventario.getTienda().getNombre());
        }
        return dto;
    }

    @Transactional
    public InventarioResponseDTO crearInventario(InventarioRequestDTO inventarioDto) {
        Producto producto = productoRepository.findById(inventarioDto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + inventarioDto.getProductoId()));

        Tienda tienda = tiendaRepository.findById(inventarioDto.getTiendaId())
                .orElseThrow(() -> new RuntimeException("Tienda no encontrada con id " + inventarioDto.getTiendaId()));

        // *** CAMBIO AQUÍ: Validar unicidad de producto y tienda ***
        // Busca si ya existe un registro de inventario para este producto en esta tienda
        Optional<Inventario> existingInventory = inventarioRepository.findByProductoAndTienda(producto, tienda);
        if (existingInventory.isPresent()) {
            throw new RuntimeException("Ya existe un registro de inventario para el producto " + producto.getNombre() + " en la tienda " + tienda.getNombre() + ".");
        }

        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidadDisponible(inventarioDto.getCantidadDisponible());
        inventario.setPuntoReorden(inventarioDto.getPuntoReorden());
        inventario.setTienda(tienda);

        Inventario inventarioGuardado = inventarioRepository.save(inventario);

        // Opcional: Para mantener la consistencia de las listas en las entidades si las usas
        // producto.getInventarios().add(inventarioGuardado); // Ya manejado por cascade
        // tienda.getInventarios().add(inventarioGuardado);   // Ya manejado por cascade

        return convertirAResponseDto(inventarioGuardado);
    }

    @Transactional(readOnly = true)
    public List<InventarioResponseDTO> obtenerTodoElInventario() {
        return inventarioRepository.findAll().stream()
                .map(this::convertirAResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<InventarioResponseDTO> obtenerInventarioPorId(Long id) {
        return inventarioRepository.findById(id)
                .map(this::convertirAResponseDto);
    }

    @Transactional
    public InventarioResponseDTO actualizarInventario(Long id, InventarioRequestDTO inventarioDto) {
        return inventarioRepository.findById(id).map(inventarioExistente -> {
            // Se actualizan la cantidad disponible y el punto de reorden.
            // Las relaciones producto y tienda no deben cambiarse en una actualización de inventario.
            // Si se necesitara "mover" inventario entre tiendas o reasignar un producto,
            // se crearían métodos de negocio específicos para ello, no una simple actualización.
            inventarioExistente.setCantidadDisponible(inventarioDto.getCantidadDisponible());
            inventarioExistente.setPuntoReorden(inventarioDto.getPuntoReorden());
            // No permitir actualizar productoId o tiendaId directamente desde aquí
            // Si el DTO los trae, ignorarlos o lanzar un error si se intenta cambiar.
            // Para mantener la simplicidad, solo actualizamos los campos de cantidad.

            Inventario inventarioActualizado = inventarioRepository.save(inventarioExistente);
            return convertirAResponseDto(inventarioActualizado);
        }).orElseThrow(() -> new RuntimeException("Registro de inventario no encontrado con id " + id));
    }

    @Transactional
    public void eliminarInventario(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new RuntimeException("Registro de inventario no encontrado con id " + id);
        }
        inventarioRepository.deleteById(id);
    }
}