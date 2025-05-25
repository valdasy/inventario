package com.tralaleritos.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    @PositiveOrZero(message = "El precio debe ser un número positivo o cero")
    private BigDecimal precio;

    @NotNull(message = "El ID del proveedor no puede ser nulo")
    private Long proveedorId; // Referencia al ID del proveedor

    @NotNull(message = "El ID de la categoría no puede ser nulo")
    private Long categoriaId; // Referencia al ID de la categoría
}