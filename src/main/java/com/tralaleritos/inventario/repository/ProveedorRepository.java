package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    // Puedes añadir métodos de búsqueda personalizados aquí si los necesitas en el futuro
}