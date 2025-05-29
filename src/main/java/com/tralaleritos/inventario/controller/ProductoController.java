package com.tralaleritos.inventario.controller;

import com.tralaleritos.inventario.model.Producto;
import com.tralaleritos.inventario.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // --- Endpoint para crear un producto ---
    // JSON de entrada puede ser:
    // { "nombre": "P1", "precio": 10.0 }
    // { "nombre": "P2", "precio": 20.0, "categoria": {"id": 1} }
    // { "nombre": "P3", "precio": 30.0, "proveedor": {"id": 1} }
    // { "nombre": "P4", "precio": 40.0, "categoria": {"id": 1}, "proveedor": {"id": 1} }
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.crearProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // --- Endpoint para obtener todos los productos ---
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }

    // --- Endpoint para obtener un producto por su ID ---
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- Endpoint para actualizar un producto ---
    // JSON de entrada puede incluir campos a cambiar. Para desasignar categoría/proveedor:
    // { "categoria": null } o { "proveedor": null }
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    // --- Endpoint para eliminar un producto ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoint para obtener productos por ID de categoría ---
    // URL: /api/productos?categoriaId=1
    @GetMapping(params = "categoriaId")
    public ResponseEntity<List<Producto>> obtenerProductosPorCategoria(@RequestParam Long categoriaId) {
        List<Producto> productos = productoService.obtenerProductosPorCategoriaId(categoriaId);
        return ResponseEntity.ok(productos); // Devuelve lista vacía si la categoría existe pero no tiene productos
                                           // o si la categoría no existe y el servicio devuelve lista vacía.
    }

    // --- Endpoint para obtener productos por ID de proveedor ---
    // URL: /api/productos?proveedorId=1
    @GetMapping(params = "proveedorId")
    public ResponseEntity<List<Producto>> obtenerProductosPorProveedor(@RequestParam Long proveedorId) {
        List<Producto> productos = productoService.obtenerProductosPorProveedorId(proveedorId);
        return ResponseEntity.ok(productos);
    }

    // --- Endpoint para obtener productos sin categoría asignada ---
    // URL: /api/productos/sin-categoria
    @GetMapping("/sin-categoria")
    public ResponseEntity<List<Producto>> obtenerProductosSinCategoria() {
        List<Producto> productos = productoService.obtenerProductosSinCategoria();
        return ResponseEntity.ok(productos);
    }

    // --- Endpoint para obtener productos sin proveedor asignado ---
    // URL: /api/productos/sin-proveedor
    @GetMapping("/sin-proveedor")
    public ResponseEntity<List<Producto>> obtenerProductosSinProveedor() {
        // El servicio maneja el null para buscar los que no tienen proveedor
        List<Producto> productos = productoService.obtenerProductosPorProveedorId(null);
        return ResponseEntity.ok(productos);
    }
}