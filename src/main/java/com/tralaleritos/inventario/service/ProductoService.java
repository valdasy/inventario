package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.model.Categoria;
import com.tralaleritos.inventario.model.Producto;
import com.tralaleritos.inventario.model.Proveedor;
import com.tralaleritos.inventario.repository.CategoriaRepository;
import com.tralaleritos.inventario.repository.ProductoRepository;
import com.tralaleritos.inventario.repository.ProveedorRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections; // Para Collections.emptyList()
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository; // Necesario para cargar/validar Proveedor

    @Autowired
    private CategoriaRepository categoriaRepository; // Necesario para cargar/validar Categoria

    @Transactional
    public Producto crearProducto(Producto producto) {
        // Validar nombre único
        if (producto.getNombre() != null && productoRepository.findByNombre(producto.getNombre()).isPresent()) {
            throw new EntityExistsException("Ya existe un producto con el nombre: " + producto.getNombre());
        }

        // Cargar Proveedor si se proporciona un ID en el objeto producto
        if (producto.getProveedor() != null && producto.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorRepository.findById(producto.getProveedor().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con id: " + producto.getProveedor().getId()));
            producto.setProveedor(proveedor);
        } else {
            // Si no se proporciona proveedor o el ID es null, se establece como null en el producto
            producto.setProveedor(null);
        }

        // Cargar Categoría si se proporciona un ID en el objeto producto
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + producto.getCategoria().getId()));
            producto.setCategoria(categoria);
        } else {
            // Si no se proporciona categoría o el ID es null, se establece como null en el producto
            producto.setCategoria(null);
        }
        
        producto.setId(null); // Asegurar que es una nueva creación
        return productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Transactional
    public Producto actualizarProducto(Long id, Producto productoActualizacion) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));

        // Validar nombre único si se está cambiando
        if (productoActualizacion.getNombre() != null && 
            !productoActualizacion.getNombre().equals(productoExistente.getNombre()) &&
            productoRepository.findByNombre(productoActualizacion.getNombre()).isPresent()) {
            throw new EntityExistsException("Ya existe otro producto con el nombre: " + productoActualizacion.getNombre());
        }
        
        // Actualizar campos simples (si vienen en el request)
        if (productoActualizacion.getNombre() != null) {
            productoExistente.setNombre(productoActualizacion.getNombre());
        }
        if (productoActualizacion.getDescripcion() != null) {
            productoExistente.setDescripcion(productoActualizacion.getDescripcion());
        }
        // Asume que precio puede ser null si así lo definiste en la entidad como opcional
        // Si se envía un precio, se actualiza. Si se envía precio:null, se pone a null.
        // Si no se envía el campo precio, productoActualizacion.getPrecio() será null,
        // y si no quieres que eso ponga el precio a null, necesitas otra lógica.
        // Aquí, por simplicidad, si viene null (o no viene), se actualiza a null o no se toca si ya es null.
        // Si quieres que solo se actualice si no es null:
        if (productoActualizacion.getPrecio() != null) {
             productoExistente.setPrecio(productoActualizacion.getPrecio());
        } else {
            // Si permites poner el precio a null explícitamente
            // productoExistente.setPrecio(null); 
            // Si no enviar precio significa "no cambiar", entonces no hagas nada aquí.
            // Por ahora, asumamos que si viene en el DTO (aunque sea null) es el valor deseado.
            // Esto es una simplificación; en un caso real, los DTOs de actualización parcial son mejores.
            // Si el campo 'precio' no se envía en el JSON, productoActualizacion.getPrecio() será null.
            // Si el JSON envía "precio": null, también será null.
            // El código actual implica que si el JSON de actualización no tiene 'precio', o lo tiene como 'null',
            // el precio del productoExistente no se cambiará a menos que explícitamente lo pongas a null aquí.
            // Para que coincida con lo que discutimos (poner a null si viene como null):
            if (productoActualizacion.getPrecio() != null || (productoActualizacion.getPrecio() == null && productoExistente.getPrecio() != null)) {
                 // Esta lógica es un poco confusa sin DTOs.
                 // Si el DTO tiene un precio (no nulo), úsalo.
                 // Si el DTO tiene un precio explícitamente nulo, y el existente no era nulo, ponlo a nulo.
                 // Si el DTO no tiene el campo precio, productoActualizacion.getPrecio() será null.
                 // Para una actualización PATCH real, usarías un DTO con Optional o JsonInclude.Include.NON_NULL.
                 // Aquí vamos a simplificar: si viene un valor (incluso null), se usa.
                 productoExistente.setPrecio(productoActualizacion.getPrecio());
            }
        }


        // Actualizar/Asignar/Desasignar Proveedor
        if (productoActualizacion.getProveedor() != null) { // Si el objeto Proveedor en sí está presente en el JSON
            if (productoActualizacion.getProveedor().getId() != null) { // Y tiene un ID
                Proveedor nuevoProveedor = proveedorRepository.findById(productoActualizacion.getProveedor().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado con id: " + productoActualizacion.getProveedor().getId()));
                productoExistente.setProveedor(nuevoProveedor);
            } else {
                // Se envió un objeto proveedor pero sin ID (ej. "proveedor": {}), podría ser un error o para desasignar
                // Si "proveedor": null se envía, productoActualizacion.getProveedor() será null (manejado abajo)
                // Si quieres que "proveedor": {} lance un error, añádelo aquí.
            }
        } else if (productoActualizacion.getProveedor() == null && productoExistente.getProveedor() != null) {
            // Si el campo "proveedor" no vino en el JSON o vino como "proveedor": null
            // y el producto existente SÍ tenía un proveedor, lo desasignamos.
            productoExistente.setProveedor(null);
        }

        // Actualizar/Asignar/Desasignar Categoría (lógica similar)
        if (productoActualizacion.getCategoria() != null) {
            if (productoActualizacion.getCategoria().getId() != null) {
                Categoria nuevaCategoria = categoriaRepository.findById(productoActualizacion.getCategoria().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + productoActualizacion.getCategoria().getId()));
                productoExistente.setCategoria(nuevaCategoria);
            }
        } else if (productoActualizacion.getCategoria() == null && productoExistente.getCategoria() != null) {
            productoExistente.setCategoria(null);
        }
        
        return productoRepository.save(productoExistente);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado con id: " + id);
        }
        // La eliminación de un producto no debería afectar a categorías o proveedores.
        // Si hay inventario asociado, CascadeType.ALL en Producto.inventarios se encargará.
        productoRepository.deleteById(id);
    }

    // --- Métodos para los nuevos endpoints de filtro ---

    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosPorCategoriaId(Long categoriaId) {
        if (categoriaId == null) {
            // Podrías decidir devolver productos sin categoría o una lista vacía/error.
            // Para ser consistentes con el endpoint /sin-categoria, aquí asumimos que se busca una categoría específica.
            // Si se quiere productos sin categoría, se usa el endpoint /sin-categoria.
            // throw new IllegalArgumentException("El ID de categoría no puede ser nulo para esta búsqueda.");
            return Collections.emptyList(); // O manejar como error.
        }
        if (!categoriaRepository.existsById(categoriaId)) {
            // throw new EntityNotFoundException("Categoría no encontrada con ID: " + categoriaId);
            return Collections.emptyList(); // Devuelve lista vacía si la categoría no existe
        }
        return productoRepository.findByCategoria_Id(categoriaId);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosPorProveedorId(Long proveedorId) {
        if (proveedorId == null) {
            // Esto se usará para el endpoint /sin-proveedor
            return productoRepository.findByProveedorIsNull();
        }
        if (!proveedorRepository.existsById(proveedorId)) {
            // throw new EntityNotFoundException("Proveedor no encontrado con ID: " + proveedorId);
            return Collections.emptyList(); // Devuelve lista vacía si el proveedor no existe
        }
        return productoRepository.findByProveedor_Id(proveedorId);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosSinCategoria() {
        return productoRepository.findByCategoriaIsNull();
    }

    // El método para "productos sin proveedor" ya está cubierto por `obtenerProductosPorProveedorId(null)`
    // que llama a `productoRepository.findByProveedorIsNull()`.
}