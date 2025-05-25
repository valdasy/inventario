package com.tralaleritos.inventario.service; 

import com.tralaleritos.inventario.dto.ProveedorRequestDTO;
import com.tralaleritos.inventario.dto.ProveedorResponseDTO;
import com.tralaleritos.inventario.model.Proveedor;
import com.tralaleritos.inventario.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    // --- Métodos de mapeo entre Entidad y DTO ---
    private Proveedor convertirADto(ProveedorRequestDTO proveedorDto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(proveedorDto.getNombre());
        proveedor.setRut(proveedorDto.getRut());
        proveedor.setDireccion(proveedorDto.getDireccion());
        proveedor.setTelefono(proveedorDto.getTelefono());
        proveedor.setEmail(proveedorDto.getEmail());
        return proveedor;
    }

    private ProveedorResponseDTO convertirAResponseDto(Proveedor proveedor) {
        ProveedorResponseDTO dto = new ProveedorResponseDTO();
        dto.setId(proveedor.getId());
        dto.setNombre(proveedor.getNombre());
        dto.setRut(proveedor.getRut());
        dto.setDireccion(proveedor.getDireccion());
        dto.setTelefono(proveedor.getTelefono());
        dto.setEmail(proveedor.getEmail());
        return dto;
    }

    // --- Operaciones CRUD con DTOs ---

    @Transactional
    public ProveedorResponseDTO crearProveedor(ProveedorRequestDTO proveedorDto) {
        Proveedor proveedor = convertirADto(proveedorDto);
        Proveedor proveedorGuardado = proveedorRepository.save(proveedor);
        return convertirAResponseDto(proveedorGuardado);
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> obtenerTodosLosProveedores() {
        return proveedorRepository.findAll().stream()
                .map(this::convertirAResponseDto) // Mapear cada entidad a un DTO de respuesta
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ProveedorResponseDTO> obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id)
                .map(this::convertirAResponseDto); // Mapear la entidad a un DTO de respuesta si existe
    }

    @Transactional
    public ProveedorResponseDTO actualizarProveedor(Long id, ProveedorRequestDTO proveedorDto) {
        return proveedorRepository.findById(id).map(proveedorExistente -> {
            proveedorExistente.setNombre(proveedorDto.getNombre());
            proveedorExistente.setRut(proveedorDto.getRut()); // <-- ¡Asegúrate de que refleje tu cambio a RUT!
            proveedorExistente.setDireccion(proveedorDto.getDireccion());
            proveedorExistente.setTelefono(proveedorDto.getTelefono());
            proveedorExistente.setEmail(proveedorDto.getEmail());
            
            Proveedor proveedorActualizado = proveedorRepository.save(proveedorExistente);
            return convertirAResponseDto(proveedorActualizado);
        }).orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id " + id));
    }

    @Transactional
    public void eliminarProveedor(Long id) {
        if (!proveedorRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado con id " + id);
        }
        proveedorRepository.deleteById(id);
    }
}