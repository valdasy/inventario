package com.tralaleritos.inventario.service;

import com.tralaleritos.inventario.dto.TiendaRequestDTO;
import com.tralaleritos.inventario.dto.TiendaResponseDTO;
import com.tralaleritos.inventario.model.Tienda;
import com.tralaleritos.inventario.repository.TiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TiendaService {

    @Autowired
    private TiendaRepository tiendaRepository;

    private Tienda convertirADto(TiendaRequestDTO tiendaDto) {
        Tienda tienda = new Tienda();
        tienda.setNombre(tiendaDto.getNombre());
        tienda.setDireccion(tiendaDto.getDireccion());
        tienda.setTelefono(tiendaDto.getTelefono());
        tienda.setEmail(tiendaDto.getEmail());
        return tienda;
    }

    private TiendaResponseDTO convertirAResponseDto(Tienda tienda) {
        TiendaResponseDTO dto = new TiendaResponseDTO();
        dto.setId(tienda.getId());
        dto.setNombre(tienda.getNombre());
        dto.setDireccion(tienda.getDireccion());
        dto.setTelefono(tienda.getTelefono());
        dto.setEmail(tienda.getEmail());
        return dto;
    }

    @Transactional
    public TiendaResponseDTO crearTienda(TiendaRequestDTO tiendaDto) {
        Tienda tienda = convertirADto(tiendaDto);
        Tienda tiendaGuardada = tiendaRepository.save(tienda);
        return convertirAResponseDto(tiendaGuardada);
    }

    @Transactional(readOnly = true)
    public List<TiendaResponseDTO> obtenerTodasLasTiendas() {
        return tiendaRepository.findAll().stream()
                .map(this::convertirAResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<TiendaResponseDTO> obtenerTiendaPorId(Long id) {
        return tiendaRepository.findById(id)
                .map(this::convertirAResponseDto);
    }

    @Transactional
    public TiendaResponseDTO actualizarTienda(Long id, TiendaRequestDTO tiendaDto) {
        return tiendaRepository.findById(id).map(tiendaExistente -> {
            tiendaExistente.setNombre(tiendaDto.getNombre());
            tiendaExistente.setDireccion(tiendaDto.getDireccion());
            tiendaExistente.setTelefono(tiendaDto.getTelefono());
            tiendaExistente.setEmail(tiendaDto.getEmail());
            Tienda tiendaActualizada = tiendaRepository.save(tiendaExistente);
            return convertirAResponseDto(tiendaActualizada);
        }).orElseThrow(() -> new RuntimeException("Tienda no encontrada con id " + id));
    }

    @Transactional
    public void eliminarTienda(Long id) {
        if (!tiendaRepository.existsById(id)) {
            throw new RuntimeException("Tienda no encontrada con id " + id);
        }
        tiendaRepository.deleteById(id);
    }
}