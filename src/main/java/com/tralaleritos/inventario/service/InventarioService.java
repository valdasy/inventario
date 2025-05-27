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

    // *** NUEVO MÉTODO ***
    @Transactional(readOnly = true)
    public List<InventarioResponseDTO> obtenerInventarioPorTienda(Long tiendaId) {
        // Primero, verifica si la tienda existe
        if (!tiendaRepository.existsById(tiendaId)) {
            throw new RuntimeException("Tienda no encontrada con ID: " + tiendaId);
        }
        // Luego, busca los inventarios asociados a esa tienda
        return inventarioRepository.findByTiendaId(tiendaId).stream()
                .map(this::convertirAResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public InventarioResponseDTO actualizarInventario(Long id, InventarioRequestDTO inventarioDto) {
        return inventarioRepository.findById(id).map(inventarioExistente -> {
            inventarioExistente.setCantidadDisponible(inventarioDto.getCantidadDisponible());
            inventarioExistente.setPuntoReorden(inventarioDto.getPuntoReorden());
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