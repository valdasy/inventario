package com.tralaleritos.inventario.controller;

import com.tralaleritos.inventario.dto.TiendaRequestDTO;
import com.tralaleritos.inventario.dto.TiendaResponseDTO;
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
    public ResponseEntity<TiendaResponseDTO> crearTienda(@Valid @RequestBody TiendaRequestDTO tiendaDto) {
        TiendaResponseDTO nuevaTienda = tiendaService.crearTienda(tiendaDto);
        return new ResponseEntity<>(nuevaTienda, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TiendaResponseDTO>> obtenerTodasLasTiendas() {
        List<TiendaResponseDTO> tiendas = tiendaService.obtenerTodasLasTiendas();
        return ResponseEntity.ok(tiendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TiendaResponseDTO> obtenerTiendaPorId(@PathVariable Long id) {
        return tiendaService.obtenerTiendaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TiendaResponseDTO> actualizarTienda(@PathVariable Long id, @Valid @RequestBody TiendaRequestDTO tiendaDto) {
        TiendaResponseDTO tiendaActualizada = tiendaService.actualizarTienda(id, tiendaDto);
        return ResponseEntity.ok(tiendaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTienda(@PathVariable Long id) {
        tiendaService.eliminarTienda(id);
        return ResponseEntity.noContent().build();
    }
}