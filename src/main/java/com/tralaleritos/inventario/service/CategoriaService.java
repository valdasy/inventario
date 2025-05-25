package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.dto.CategoriaRequestDTO;
import com.tralaleritos.inventario.dto.CategoriaResponseDTO;
import com.tralaleritos.inventario.model.Categoria;
import com.tralaleritos.inventario.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Métodos de mapeo
    private Categoria convertirADto(CategoriaRequestDTO categoriaDto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDto.getNombre());
        categoria.setDescripcion(categoriaDto.getDescripcion());
        return categoria;
    }

    private CategoriaResponseDTO convertirAResponseDto(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setDescripcion(categoria.getDescripcion());
        return dto;
    }

    @Transactional
    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO categoriaDto) {
        Categoria categoria = convertirADto(categoriaDto);
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertirAResponseDto(categoriaGuardada);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirAResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<CategoriaResponseDTO> obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(this::convertirAResponseDto);
    }

    @Transactional
    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO categoriaDto) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            categoriaExistente.setNombre(categoriaDto.getNombre());
            categoriaExistente.setDescripcion(categoriaDto.getDescripcion());
            Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);
            return convertirAResponseDto(categoriaActualizada);
        }).orElseThrow(() -> new RuntimeException("Categoría no encontrada con id " + id));
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con id " + id);
        }
        categoriaRepository.deleteById(id);
    }
}