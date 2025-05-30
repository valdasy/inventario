package com.tralaleritos.inventario.controller;

import com.tralaleritos.inventario.dto.TiendaConInventarioDetalladoResponseDTO;
import com.tralaleritos.inventario.model.Tienda; // Si aún tienes endpoints que devuelven la entidad
import com.tralaleritos.inventario.service.TiendaService;
import jakarta.validation.Valid; // Si usas @Valid para crear/actualizar
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tiendas")
public class TiendaController {

    @Autowired
    private TiendaService tiendaService;

    // --- Nuevo Endpoint para obtener tienda con inventario detallado ---
    @GetMapping("/{id}/detalles-inventario")
    public ResponseEntity<TiendaConInventarioDetalladoResponseDTO> obtenerTiendaConInventario(@PathVariable Long id) {
        return tiendaService.obtenerTiendaConInventarioDetallado(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- Tus Endpoints CRUD existentes para Tienda (pueden devolver la entidad o un DTO simple) ---
    @PostMapping
    public ResponseEntity<Tienda> crearTienda(@Valid @RequestBody Tienda tienda) { // O un TiendaRequestDTO
        Tienda nuevaTienda = tiendaService.crearTienda(tienda);
        return new ResponseEntity<>(nuevaTienda, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tienda>> obtenerTodasLasTiendas() {
        // Esto devolverá las Tiendas sin el inventario si Tienda.inventarios tiene @JsonIgnore
        List<Tienda> tiendas = tiendaService.obtenerTodasLasTiendasSimples();
        return ResponseEntity.ok(tiendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tienda> obtenerTiendaPorId(@PathVariable Long id) {
        // Esto devolverá la Tienda sin el inventario
        return tiendaService.obtenerTiendaPorIdSimple(id) // Llama al método de servicio adecuado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tienda> actualizarTienda(@PathVariable Long id, @Valid @RequestBody Tienda tienda) { // O un TiendaRequestDTO
        Tienda tiendaActualizada = tiendaService.actualizarTienda(id, tienda);
        return ResponseEntity.ok(tiendaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTienda(@PathVariable Long id) {
        tiendaService.eliminarTienda(id);
        return ResponseEntity.noContent().build();
    }
}