package com.tralaleritos.inventario.controller;

import com.tralaleritos.inventario.model.Inventario;
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
    public ResponseEntity<Inventario> crearInventario(@Valid @RequestBody Inventario inventario) {
        // El JSON de entrada debe ser algo como:
        // {
        //   "producto": { "id": 1 },
        //   "tienda": { "id": 1 },
        //   "cantidadDisponible": 100,
        //   "puntoReorden": 20
        // }
        Inventario nuevoInventario = inventarioService.crearInventario(inventario);
        return new ResponseEntity<>(nuevoInventario, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> obtenerTodoElInventario() {
        List<Inventario> inventario = inventarioService.obtenerTodoElInventario();
        return ResponseEntity.ok(inventario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerInventarioPorId(@PathVariable Long id) {
        return inventarioService.obtenerInventarioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/por-tienda/{tiendaId}")
    public ResponseEntity<List<Inventario>> obtenerInventarioPorTienda(@PathVariable Long tiendaId) {
        List<Inventario> inventarios = inventarioService.obtenerInventarioPorTienda(tiendaId);
        if (inventarios.isEmpty()) {
            // Podrías devolver notFound si la tienda existe pero no tiene inventario,
            // o siempre ok con lista vacía. Ok con lista vacía suele ser más estándar.
            // La excepción EntityNotFoundException se lanzará desde el servicio si la tienda no existe.
        }
        return ResponseEntity.ok(inventarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarInventario(@PathVariable Long id, @Valid @RequestBody Inventario inventario) {
        // El JSON de entrada debe ser algo como:
        // {
        //   "cantidadDisponible": 90, (opcional)
        //   "puntoReorden": 15       (opcional)
        // }
        // No se deben enviar producto.id ni tienda.id en la actualización aquí,
        // ya que la lógica del servicio no los modifica.
        Inventario inventarioActualizado = inventarioService.actualizarInventario(id, inventario);
        return ResponseEntity.ok(inventarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInventario(@PathVariable Long id) {
        inventarioService.eliminarInventario(id);
        return ResponseEntity.noContent().build();
    }
}