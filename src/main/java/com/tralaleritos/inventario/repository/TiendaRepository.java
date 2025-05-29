package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Tienda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TiendaRepository extends JpaRepository<Tienda, Long> {
    // Métodos para verificar unicidad si los necesitas
    Optional<Tienda> findByNombre(String nombre);
    Optional<Tienda> findByEmail(String email); // Solo si el email es obligatorio y único
}