package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Inventario;
import com.tralaleritos.inventario.model.Producto;
import com.tralaleritos.inventario.model.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // Para verificar la restricción de unicidad (producto_id, tienda_id)
    Optional<Inventario> findByProductoAndTienda(Producto producto, Tienda tienda);
    // Alternativa usando IDs directamente, útil si solo tienes los IDs
    Optional<Inventario> findByProducto_IdAndTienda_Id(Long productoId, Long tiendaId);


    // Para buscar todos los inventarios de una tienda específica
    List<Inventario> findByTienda_Id(Long tiendaId);

    // Para buscar todos los inventarios de un producto específico (en todas las tiendas)
    List<Inventario> findByProducto_Id(Long productoId);
}