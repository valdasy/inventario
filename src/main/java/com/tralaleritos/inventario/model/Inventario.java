package com.tralaleritos.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventarios", uniqueConstraints = { // *** NUEVA ADICIÓN DE CLAVE ÚNICA COMPUESTA ***
    @UniqueConstraint(columnNames = {"producto_id", "tienda_id"})
})
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // *** CAMBIO AQUÍ: Relación ManyToOne con Producto ***
    // Muchos registros de inventario pueden referirse a un mismo producto (en diferentes tiendas)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false) // Ya no es unique = true aquí
    @NotNull(message = "El producto asociado al inventario no puede ser nulo")
    private Producto producto;

    @NotNull(message = "La cantidad disponible no puede ser nula")
    @Min(value = 0, message = "La cantidad disponible no puede ser negativa")
    private Integer cantidadDisponible;

    @NotNull(message = "El punto de reorden no puede ser nulo")
    @Min(value = 0, message = "El punto de reorden no puede ser negativo")
    private Integer puntoReorden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tienda_id", nullable = false)
    @NotNull(message = "La tienda asociada al inventario no puede ser nulo")
    private Tienda tienda;
}