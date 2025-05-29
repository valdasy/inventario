package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Por si añades búsquedas que puedan no devolver nada

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Puedes añadir métodos de búsqueda personalizados aquí si los necesitas.
    // Por ejemplo, para verificar si ya existe una categoría con ese nombre:
    Optional<Categoria> findByNombre(String nombre);
}