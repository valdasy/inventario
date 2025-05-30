package com.tralaleritos.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal; // Asegúrate de tener este import

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDetalladoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private CategoriaSimpleResponseDTO categoria;   // Referencia al DTO simple de Categoría
    private ProveedorSimpleResponseDTO proveedor; // Referencia al DTO simple de Proveedor
}