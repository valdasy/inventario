package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    // Métodos para verificar unicidad
    Optional<Proveedor> findByRut(String rut);
    Optional<Proveedor> findByEmail(String email);
    // Podrías incluso buscar por nombre si lo necesitas
    // Optional<Proveedor> findByNombre(String nombre);
}