package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.dto.*; // Asegúrate de tener todos los DTOs en este paquete
import com.tralaleritos.inventario.model.*; // Asegúrate de tener todas las entidades
import com.tralaleritos.inventario.repository.InventarioRepository;
import com.tralaleritos.inventario.repository.TiendaRepository;
import com.tralaleritos.inventario.repository.ProductoRepository; // Necesario si vas a verificar existencia de producto en otros métodos
import com.tralaleritos.inventario.repository.CategoriaRepository; // Necesario para el mapper de producto
import com.tralaleritos.inventario.repository.ProveedorRepository; // Necesario para el mapper de producto

import jakarta.persistence.EntityExistsException; // Para validaciones de unicidad
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepository;

    @Autowired
    private InventarioRepository inventarioRepository; // Inyectado para el método detallado

    // MAPPERS (Considera moverlos a una clase de utilidad Mapper si se vuelven muchos)
    // Estos mappers son para construir los DTOs de respuesta.

    private CategoriaSimpleResponseDTO toCategoriaSimpleDTO(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        return new CategoriaSimpleResponseDTO(categoria.getId(), categoria.getNombre());
    }

    private ProveedorSimpleResponseDTO toProveedorSimpleDTO(Proveedor proveedor) {
        if (proveedor == null) {
            return null;
        }
        return new ProveedorSimpleResponseDTO(proveedor.getId(), proveedor.getNombre());
    }

    private ProductoDetalladoResponseDTO toProductoDetalladoDTO(Producto producto) {
        if (producto == null) {
            return null;
        }
        // Asume que producto.getCategoria() y producto.getProveedor() devolverán las entidades
        // cargadas (gracias al EntityGraph en el repositorio de inventario) o serán null
        // si el producto no tiene categoría/proveedor.
        return new ProductoDetalladoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                toCategoriaSimpleDTO(producto.getCategoria()),
                toProveedorSimpleDTO(producto.getProveedor())
        );
    }

    private InventarioDetalladoResponseDTO toInventarioDetalladoDTO(Inventario inventario) {
        if (inventario == null) {
            return null;
        }
        return new InventarioDetalladoResponseDTO(
                inventario.getId(),
                toProductoDetalladoDTO(inventario.getProducto()), // El producto aquí ya debería estar cargado
                inventario.getCantidadDisponible(),
                inventario.getPuntoReorden()
        );
    }
    // --- FIN MAPPERS ---


    // --- Método para obtener Tienda con Inventario Detallado (usando DTOs) ---
    @Transactional(readOnly = true)
    public Optional<TiendaConInventarioDetalladoResponseDTO> obtenerTiendaConInventarioDetallado(Long tiendaId) {
        Optional<Tienda> tiendaOpt = tiendaRepository.findById(tiendaId);
        if (tiendaOpt.isEmpty()) {
            return Optional.empty(); // O lanzar EntityNotFoundException si prefieres que el controlleradvice lo maneje
        }
        Tienda tienda = tiendaOpt.get();

        // Usar el método del InventarioRepository que carga los detalles con EntityGraph
        // Asumimos que se llama 'findWithDetailsByTienda_Id' o que 'findByTienda_Id' tiene el EntityGraph
        List<Inventario> inventariosDeTienda = inventarioRepository.findWithDetailsByTienda_Id(tiendaId);

        List<InventarioDetalladoResponseDTO> inventarioDTOs = inventariosDeTienda.stream()
                .map(this::toInventarioDetalladoDTO)
                .collect(Collectors.toList());

        TiendaConInventarioDetalladoResponseDTO dto = new TiendaConInventarioDetalladoResponseDTO(
                tienda.getId(),
                tienda.getNombre(),
                tienda.getDireccion(),
                tienda.getTelefono(),
                tienda.getEmail(),
                tienda.getHorariosApertura(),
                tienda.getPersonalAsignado(),
                tienda.getPoliticasLocales(),
                inventarioDTOs
        );
        return Optional.of(dto);
    }


    // --- Métodos CRUD básicos para Tienda (Devolviendo la entidad Tienda) ---
    // Estos asumen que Tienda.inventarios tiene @JsonIgnore si no quieres que se serialice
    // la lista de inventarios al devolver la entidad Tienda directamente.

    @Transactional
    public Tienda crearTienda(Tienda tienda) {
        // Validar unicidad de Nombre (ejemplo)
        if (tienda.getNombre() != null && tiendaRepository.findByNombre(tienda.getNombre()).isPresent()) {
            throw new EntityExistsException("Ya existe una tienda con el nombre: " + tienda.getNombre());
        }
        // Validar unicidad de Email (si es obligatorio y único, y no es null/vacío)
        if (tienda.getEmail() != null && !tienda.getEmail().isEmpty()) {
            if (tiendaRepository.findByEmail(tienda.getEmail()).isPresent()) {
                throw new EntityExistsException("Ya existe una tienda con el email: " + tienda.getEmail());
            }
        }
        // Asegurarse de que el ID sea nulo para la creación
        tienda.setId(null);
        return tiendaRepository.save(tienda);
    }

    @Transactional(readOnly = true)
    public List<Tienda> obtenerTodasLasTiendasSimples() {
        // Devuelve la lista de entidades Tienda.
        // Si Tienda.inventarios tiene @JsonIgnore, esa lista no se serializará.
        return tiendaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Tienda> obtenerTiendaPorIdSimple(Long id) {
        // Devuelve la entidad Tienda.
        // Si Tienda.inventarios tiene @JsonIgnore, esa lista no se serializará.
        return tiendaRepository.findById(id);
    }

    @Transactional
    public Tienda actualizarTienda(Long id, Tienda tiendaActualizacion) {
        Tienda tiendaExistente = tiendaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tienda no encontrada con id: " + id));

        // Validar unicidad de Nombre si se está cambiando (ejemplo)
        if (tiendaActualizacion.getNombre() != null && 
            !tiendaActualizacion.getNombre().equals(tiendaExistente.getNombre()) &&
            tiendaRepository.findByNombre(tiendaActualizacion.getNombre()).isPresent()) {
            throw new EntityExistsException("Ya existe otra tienda con el nombre: " + tiendaActualizacion.getNombre());
        }
        // Validar unicidad de Email si se está cambiando y no es nulo/vacío (ejemplo)
        if (tiendaActualizacion.getEmail() != null && !tiendaActualizacion.getEmail().isEmpty() &&
            !tiendaActualizacion.getEmail().equals(tiendaExistente.getEmail()) &&
            tiendaRepository.findByEmail(tiendaActualizacion.getEmail()).isPresent()) {
            throw new EntityExistsException("Ya existe otra tienda con el email: " + tiendaActualizacion.getEmail());
        }
        
        // Copiar campos actualizables
        tiendaExistente.setNombre(tiendaActualizacion.getNombre());
        tiendaExistente.setDireccion(tiendaActualizacion.getDireccion());
        tiendaExistente.setTelefono(tiendaActualizacion.getTelefono());
        tiendaExistente.setEmail(tiendaActualizacion.getEmail());
        tiendaExistente.setHorariosApertura(tiendaActualizacion.getHorariosApertura());
        tiendaExistente.setPersonalAsignado(tiendaActualizacion.getPersonalAsignado());
        tiendaExistente.setPoliticasLocales(tiendaActualizacion.getPoliticasLocales());
        
        return tiendaRepository.save(tiendaExistente);
    }

    @Transactional
    public void eliminarTienda(Long id) {
        if (!tiendaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tienda no encontrada con id " + id);
        }
        // Considera la lógica de desvinculación o cascada para inventarios si es necesario.
        // Si Tienda.inventarios tiene CascadeType.ALL y orphanRemoval=true,
        // eliminar la tienda eliminará sus registros de inventario.
        tiendaRepository.deleteById(id);
    }
}