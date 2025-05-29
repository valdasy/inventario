package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Producto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // --- Métodos CRUD básicos heredados de JpaRepository ---
    // save(), findById(), findAll(), deleteById(), etc.

    // --- Métodos de búsqueda personalizados ---

    // Para verificar unicidad de nombre de producto
    Optional<Producto> findByNombre(String nombre);

    // --- Métodos con @EntityGraph para cargar relaciones LAZY de forma EAGER selectivamente ---
    // Esto es para evitar LazyInitializationException cuando serializas el Producto
    // y quieres que sus relaciones 'proveedor', 'categoria', e 'inventarios' estén cargadas.

    @Override
    @EntityGraph(attributePaths = {"proveedor", "categoria", "inventarios"})
    Optional<Producto> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"proveedor", "categoria", "inventarios"})
    List<Producto> findAll(); // Si quieres que todas las relaciones se carguen al obtener todos los productos

    // Para obtener productos por ID de categoría, cargando también proveedor e inventarios del producto
    @EntityGraph(attributePaths = {"proveedor", "categoria", "inventarios"})
    List<Producto> findByCategoria_Id(Long categoriaId);

    // Para obtener productos por ID de proveedor, cargando también categoría e inventarios del producto
    @EntityGraph(attributePaths = {"proveedor", "categoria", "inventarios"})
    List<Producto> findByProveedor_Id(Long proveedorId);

    // Para obtener productos que no tienen categoría asignada
    // Aquí también podrías querer cargar proveedor e inventarios si es relevante
    @EntityGraph(attributePaths = {"proveedor", "inventarios"}) // Categoria es null, así que no se incluye
    List<Producto> findByCategoriaIsNull();

    // Para obtener productos que no tienen proveedor asignado
    // Aquí también podrías querer cargar categoría e inventarios si es relevante
    @EntityGraph(attributePaths = {"categoria", "inventarios"}) // Proveedor es null, así que no se incluye
    List<Producto> findByProveedorIsNull();

}