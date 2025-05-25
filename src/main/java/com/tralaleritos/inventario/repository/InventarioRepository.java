package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Inventario;
import com.tralaleritos.inventario.model.Producto; // Importar Producto
import com.tralaleritos.inventario.model.Tienda;   // Importar Tienda
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importar Optional

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    // *** NUEVO MÉTODO PARA VALIDACIÓN DE UNICIDAD ***
    Optional<Inventario> findByProductoAndTienda(Producto producto, Tienda tienda);
}