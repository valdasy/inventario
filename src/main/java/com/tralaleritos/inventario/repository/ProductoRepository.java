package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // JpaRepository<TipoDeEntidad, TipoDeIdDeLaEntidad>
    // Automáticamente obtendrás métodos como: save(), findById(), findAll(), deleteById(), etc.

    // Puedes añadir métodos de búsqueda personalizados aquí, y Spring Data JPA los implementará:
    // List<Producto> findByNombreContainingIgnoreCase(String nombre);
    // List<Producto> findByCategoriaId(Long categoriaId);
    // List<Producto> findByProveedorId(Long proveedorId);
}