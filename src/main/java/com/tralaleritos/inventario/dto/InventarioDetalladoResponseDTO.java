package com.tralaleritos.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDetalladoResponseDTO {
    private Long id; // ID del registro de inventario
    private ProductoDetalladoResponseDTO producto; // Referencia al DTO detallado de Producto
    private Integer cantidadDisponible;
    private Integer puntoReorden;
    // No incluimos la tienda aquí porque este DTO se usará dentro de la respuesta de Tienda
}