package com.tralaleritos.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioResponseDTO {

    private Long id;
    private Long productoId; // ID del producto asociado
    private String nombreProducto; // Nombre del producto asociado (para fácil visualización)
    private Integer cantidadDisponible;
    private Integer puntoReorden;
    private Long tiendaId; // *** NUEVA ADICIÓN: ID de la tienda asociada ***
    private String nombreTienda; // *** NUEVA ADICIÓN: Nombre de la tienda asociada (para fácil visualización) ***
}