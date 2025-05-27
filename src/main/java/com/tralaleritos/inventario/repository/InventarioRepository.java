package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Inventario;
import com.tralaleritos.inventario.model.Producto;
import com.tralaleritos.inventario.model.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importar List
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByProductoAndTienda(Producto producto, Tienda tienda);

    // *** NUEVO MÉTODO ***
    // Spring Data JPA automáticamente entiende que quieres buscar inventarios
    // donde el campo 'tienda' (en la entidad Inventario) tiene un 'id' específico.
    List<Inventario> findByTiendaId(Long tiendaId);
}