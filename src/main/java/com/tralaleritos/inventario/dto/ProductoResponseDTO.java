package com.tralaleritos.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Long proveedorId;
    private String nombreProveedor; // Para facilitar la visualización en la respuesta
    private Long categoriaId;
    private String nombreCategoria; // Para facilitar la visualización en la respuesta
}