package com.tralaleritos.inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioRequestDTO {

    @NotNull(message = "El ID del producto no puede ser nulo")
    private Long productoId; // Referencia al ID del producto

    @NotNull(message = "La cantidad disponible no puede ser nula")
    @Min(value = 0, message = "La cantidad disponible no puede ser negativa")
    private Integer cantidadDisponible;

    @NotNull(message = "El punto de reorden no puede ser nulo")
    @Min(value = 0, message = "El punto de reorden no puede ser negativo")
    private Integer puntoReorden;

    @NotNull(message = "El ID de la tienda no puede ser nulo")
    private Long tiendaId; // *** NUEVA ADICIÓN: Referencia al ID de la tienda ***
}