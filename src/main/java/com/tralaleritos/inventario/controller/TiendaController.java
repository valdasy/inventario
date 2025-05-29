package com.tralaleritos.inventario.controller;

import com.tralaleritos.inventario.model.Tienda;
import com.tralaleritos.inventario.service.TiendaService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<Tienda> crearTienda(@Valid @RequestBody Tienda tienda) {
        Tienda nuevaTienda = tiendaService.crearTienda(tienda);
        return new ResponseEntity<>(nuevaTienda, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tienda>> obtenerTodasLasTiendas() {
        List<Tienda> tiendas = tiendaService.obtenerTodasLasTiendas();
        return ResponseEntity.ok(tiendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tienda> obtenerTiendaPorId(@PathVariable Long id) {
        return tiendaService.obtenerTiendaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tienda> actualizarTienda(@PathVariable Long id, @Valid @RequestBody Tienda tienda) {
        Tienda tiendaActualizada = tiendaService.actualizarTienda(id, tienda);
        return ResponseEntity.ok(tiendaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTienda(@PathVariable Long id) {
        tiendaService.eliminarTienda(id);
        return ResponseEntity.noContent().build();
    }
}