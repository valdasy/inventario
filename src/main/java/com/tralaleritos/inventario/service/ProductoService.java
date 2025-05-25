package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.dto.ProductoRequestDTO;
import com.tralaleritos.inventario.dto.ProductoResponseDTO;
import com.tralaleritos.inventario.model.Categoria;
import com.tralaleritos.inventario.model.Producto;
import com.tralaleritos.inventario.model.Proveedor;
import com.tralaleritos.inventario.repository.CategoriaRepository;
import com.tralaleritos.inventario.repository.ProductoRepository;
import com.tralaleritos.inventario.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository; // Necesario para buscar el proveedor

    @Autowired
    private CategoriaRepository categoriaRepository; // Necesario para buscar la categoría

    // Métodos de mapeo
    private Producto convertirADto(ProductoRequestDTO productoDto) {
        Producto producto = new Producto();
        producto.setNombre(productoDto.getNombre());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setPrecio(productoDto.getPrecio());
        // El proveedor y la categoría se asignan en el método crear/actualizar
        return producto;
    }

    private ProductoResponseDTO convertirAResponseDto(Producto producto) {
        ProductoResponseDTO dto = new ProductoResponseDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        if (producto.getProveedor() != null) {
            dto.setProveedorId(producto.getProveedor().getId());
            dto.setNombreProveedor(producto.getProveedor().getNombre());
        }
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setNombreCategoria(producto.getCategoria().getNombre());
        }
        return dto;
    }

    @Transactional
    public ProductoResponseDTO crearProducto(ProductoRequestDTO productoDto) {
        // Buscar proveedor y categoría para asociar
        Proveedor proveedor = proveedorRepository.findById(productoDto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id " + productoDto.getProveedorId()));
        Categoria categoria = categoriaRepository.findById(productoDto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + productoDto.getCategoriaId()));

        Producto producto = convertirADto(productoDto);
        producto.setProveedor(proveedor);
        producto.setCategoria(categoria);

        Producto productoGuardado = productoRepository.save(producto);
        return convertirAResponseDto(productoGuardado);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerTodosLosProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertirAResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProductoResponseDTO> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .map(this::convertirAResponseDto);
    }

    @Transactional
    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO productoDto) {
        return productoRepository.findById(id).map(productoExistente -> {
            productoExistente.setNombre(productoDto.getNombre());
            productoExistente.setDescripcion(productoDto.getDescripcion());
            productoExistente.setPrecio(productoDto.getPrecio());

            // Actualizar proveedor si el ID es diferente
            if (!productoExistente.getProveedor().getId().equals(productoDto.getProveedorId())) {
                Proveedor nuevoProveedor = proveedorRepository.findById(productoDto.getProveedorId())
                        .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id " + productoDto.getProveedorId()));
                productoExistente.setProveedor(nuevoProveedor);
            }

            // Actualizar categoría si el ID es diferente
            if (!productoExistente.getCategoria().getId().equals(productoDto.getCategoriaId())) {
                Categoria nuevaCategoria = categoriaRepository.findById(productoDto.getCategoriaId())
                        .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + productoDto.getCategoriaId()));
                productoExistente.setCategoria(nuevaCategoria);
            }
            
            Producto productoActualizado = productoRepository.save(productoExistente);
            return convertirAResponseDto(productoActualizado);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + id));
    }

    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id " + id);
        }
        productoRepository.deleteById(id);
    }
}