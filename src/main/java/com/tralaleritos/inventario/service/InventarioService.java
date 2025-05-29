package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.model.Inventario;
import com.tralaleritos.inventario.model.Producto;
import com.tralaleritos.inventario.model.Tienda;
import com.tralaleritos.inventario.repository.InventarioRepository;
import com.tralaleritos.inventario.repository.ProductoRepository;
import com.tralaleritos.inventario.repository.TiendaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoRepository productoRepository; // Necesario para cargar Producto por ID

    @Autowired
    private TiendaRepository tiendaRepository; // Necesario para cargar Tienda por ID

    @Transactional
    public Inventario crearInventario(Inventario inventario) {
        // Validar y cargar el Producto
        if (inventario.getProducto() == null || inventario.getProducto().getId() == null) {
            throw new IllegalArgumentException("El ID del producto es requerido para crear un registro de inventario.");
        }
        Producto producto = productoRepository.findById(inventario.getProducto().getId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + inventario.getProducto().getId()));

        // Validar y cargar la Tienda
        if (inventario.getTienda() == null || inventario.getTienda().getId() == null) {
            throw new IllegalArgumentException("El ID de la tienda es requerido para crear un registro de inventario.");
        }
        Tienda tienda = tiendaRepository.findById(inventario.getTienda().getId())
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada con id: " + inventario.getTienda().getId()));

        // Verificar si ya existe un inventario para este producto en esta tienda
        if (inventarioRepository.findByProducto_IdAndTienda_Id(producto.getId(), tienda.getId()).isPresent()) {
            throw new EntityExistsException("Ya existe un registro de inventario para el producto '" + producto.getNombre() + "' en la tienda '" + tienda.getNombre() + "'.");
        }

        // Asignar las entidades cargadas y gestionadas
        inventario.setProducto(producto);
        inventario.setTienda(tienda);
        inventario.setId(null); // Asegurar la creación

        return inventarioRepository.save(inventario);
    }

    @Transactional(readOnly = true)
    public List<Inventario> obtenerTodoElInventario() {
        return inventarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Inventario> obtenerInventarioPorId(Long id) {
        return inventarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Inventario> obtenerInventarioPorTienda(Long tiendaId) {
        if (!tiendaRepository.existsById(tiendaId)) {
            throw new EntityNotFoundException("Tienda no encontrada con ID: " + tiendaId);
        }
        return inventarioRepository.findByTienda_Id(tiendaId);
    }

    @Transactional
    public Inventario actualizarInventario(Long id, Inventario inventarioActualizacion) {
        Inventario inventarioExistente = inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de inventario no encontrado con id: " + id));

        // En una actualización de inventario, normalmente solo se actualizan cantidades.
        // Cambiar el producto o la tienda de un registro de inventario existente suele ser una mala práctica
        // (sería mejor eliminar el antiguo y crear uno nuevo).
        // Por lo tanto, no permitiremos cambiar productoId ni tiendaId aquí.

        if (inventarioActualizacion.getCantidadDisponible() != null) {
            inventarioExistente.setCantidadDisponible(inventarioActualizacion.getCantidadDisponible());
        }
        if (inventarioActualizacion.getPuntoReorden() != null) {
            inventarioExistente.setPuntoReorden(inventarioActualizacion.getPuntoReorden());
        }

        // Las validaciones de Min(0) en la entidad se encargarán de los valores negativos.

        return inventarioRepository.save(inventarioExistente);
    }

    @Transactional
    public void eliminarInventario(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Registro de inventario no encontrado con id: " + id);
        }
        inventarioRepository.deleteById(id);
    }
}