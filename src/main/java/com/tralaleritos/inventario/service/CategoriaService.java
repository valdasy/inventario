package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.model.Categoria;
import com.tralaleritos.inventario.repository.CategoriaRepository;
import jakarta.persistence.EntityExistsException; // Para una excepción más específica
import jakarta.persistence.EntityNotFoundException; // Para una excepción más específica
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Ya NO se necesitan métodos de conversión a DTO

    @Transactional
    public Categoria crearCategoria(Categoria categoria) {
        // Validar si ya existe una categoría con el mismo nombre (si has añadido unique constraint)
        if (categoria.getNombre() != null && categoriaRepository.findByNombre(categoria.getNombre()).isPresent()) {
            throw new EntityExistsException("Ya existe una categoría con el nombre: " + categoria.getNombre());
        }
        // Asegurarse de que el ID sea nulo para evitar actualizar una existente por error al crear
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }

    @Transactional(readOnly = true)
    public List<Categoria> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Transactional
    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizacion) {
        // Verificar si se está intentando cambiar el nombre a uno que ya existe (y no es el de la entidad actual)
        if (categoriaActualizacion.getNombre() != null) {
            Optional<Categoria> categoriaConMismoNombre = categoriaRepository.findByNombre(categoriaActualizacion.getNombre());
            if (categoriaConMismoNombre.isPresent() && !categoriaConMismoNombre.get().getId().equals(id)) {
                throw new EntityExistsException("Ya existe otra categoría con el nombre: " + categoriaActualizacion.getNombre());
            }
        }

        return categoriaRepository.findById(id).map(categoriaExistente -> {
            categoriaExistente.setNombre(categoriaActualizacion.getNombre());
            categoriaExistente.setDescripcion(categoriaActualizacion.getDescripcion());
            // La lista de productos se gestiona a través de la entidad Producto,
            // no se actualiza directamente aquí al actualizar una categoría.
            return categoriaRepository.save(categoriaExistente);
        }).orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id " + id));
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoría no encontrada con id " + id);
        }
        // Considerar: ¿Qué sucede con los productos asociados?
        // Con CascadeType.ALL y orphanRemoval=true, los productos asociados también se eliminarán.
        // Si no es el comportamiento deseado, se debe cambiar la configuración de la cascada
        // o manejar la desvinculación de productos manualmente antes de eliminar.
        categoriaRepository.deleteById(id);
    }
}