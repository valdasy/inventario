package com.tralaleritos.inventario.repository;

import com.tralaleritos.inventario.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Puedes añadir métodos de búsqueda personalizados aquí si los necesitas en el futuro
}