package com.tralaleritos.inventario.controller;

import com.tralaleritos.inventario.dto.InventarioRequestDTO;
import com.tralaleritos.inventario.dto.InventarioResponseDTO;
import com.tralaleritos.inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crearInventario(@Valid @RequestBody InventarioRequestDTO inventarioDto) {
        InventarioResponseDTO nuevoInventario = inventarioService.crearInventario(inventarioDto);
        return new ResponseEntity<>(nuevoInventario, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> obtenerTodoElInventario() {
        List<InventarioResponseDTO> inventarios = inventarioService.obtenerTodoElInventario();
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> obtenerInventarioPorId(@PathVariable Long id) {
        return inventarioService.obtenerInventarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> actualizarInventario(@PathVariable Long id, @Valid @RequestBody InventarioRequestDTO inventarioDto) {
        InventarioResponseDTO inventarioActualizado = inventarioService.actualizarInventario(id, inventarioDto);
        return ResponseEntity.ok(inventarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }
}