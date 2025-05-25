package com.tralaleritos.inventario.controller; // <-- ¡Ajusta este paquete!

import com.tralaleritos.inventario.dto.ProveedorRequestDTO; // <-- ¡Ajusta este import!
import com.tralaleritos.inventario.dto.ProveedorResponseDTO; // <-- ¡Ajusta este import!
import com.tralaleritos.inventario.service.ProveedorService; // <-- ¡Ajusta este import!
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // POST: Crear un nuevo proveedor
    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> crearProveedor(@Valid @RequestBody ProveedorRequestDTO proveedorDto) {
        ProveedorResponseDTO nuevoProveedor = proveedorService.crearProveedor(proveedorDto);
        return new ResponseEntity<>(nuevoProveedor, HttpStatus.CREATED);
    }

    // GET: Obtener todos los proveedores
    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> obtenerTodosLosProveedores() {
        List<ProveedorResponseDTO> proveedores = proveedorService.obtenerTodosLosProveedores();
        return ResponseEntity.ok(proveedores);
    }

    // GET: Obtener un proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> obtenerProveedorPorId(@PathVariable Long id) {
        return proveedorService.obtenerProveedorPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT: Actualizar un proveedor existente por ID
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> actualizarProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorRequestDTO proveedorDto) {
        ProveedorResponseDTO proveedorActualizado = proveedorService.actualizarProveedor(id, proveedorDto);
        return ResponseEntity.ok(proveedorActualizado);
    }

    // DELETE: Eliminar un proveedor por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return ResponseEntity.noContent().build();
    }
}