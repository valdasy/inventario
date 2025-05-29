package com.tralaleritos.inventario.model;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Asumimos que la relación con Inventario sí se gestiona así
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull; // <-- QUITAMOS @NotNull para categoria y proveedor
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString(exclude = {"proveedor", "categoria", "inventarios"})
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    // @NotNull // El precio podría ser nulo si se permite crearlo sin precio inicialmente
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio debe ser cero o un número positivo")
    // @Column(nullable = false) // Podría ser nullable = true si el precio es opcional al crear
    private BigDecimal precio; // Considera si el precio también puede ser opcional al inicio

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = true) // <--- CAMBIO AQUÍ: nullable = true
    // @NotNull(message = "El proveedor no puede ser nulo") // <--- QUITADO
    @JsonIdentityReference(alwaysAsId = true) 
    private Proveedor proveedor; // Ahora puede ser null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = true) // <--- CAMBIO AQUÍ: nullable = true
    // @NotNull(message = "La categoría no puede ser nula") // <--- QUITADO
    @JsonIdentityReference(alwaysAsId = true)
    private Categoria categoria; // Ahora puede ser null

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("producto-inventarios") // Si quieres que se vea el inventario de un producto
    private List<Inventario> inventarios = new ArrayList<>();

    // Ajusta el constructor si es necesario, o añade otros
    public Producto(String nombre, String descripcion, BigDecimal precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }
    // Puedes tener otro constructor que sí incluya proveedor y categoría si a veces los pasas
}