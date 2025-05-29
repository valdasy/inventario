package com.tralaleritos.inventario.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // <<--- IMPORTANTE
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventarios", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"producto_id", "tienda_id"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString(exclude = {"producto", "tienda"})
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @NotNull(message = "El producto asociado al inventario no puede ser nulo")
    @JsonBackReference("producto-inventarios") // <--- PARA LA RELACIÓN CON Producto.inventarios
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
    // Asumimos que Tienda.inventarios usará @JsonManagedReference("tienda-inventarios") o @JsonIgnore
    // Si Tienda.inventarios tiene @JsonManagedReference("tienda-inventarios"), entonces aquí iría:
    @JsonBackReference("tienda-inventarios")
    private Tienda tienda;

    public Inventario(Producto producto, Tienda tienda, Integer cantidadDisponible, Integer puntoReorden) {
        this.producto = producto;
        this.tienda = tienda;
        this.cantidadDisponible = cantidadDisponible;
        this.puntoReorden = puntoReorden;
    }
}