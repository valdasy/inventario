package com.tralaleritos.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tiendas") // Nombre de la tabla en la base de datos
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la tienda no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "La dirección de la tienda no puede estar vacía")
    @Column(nullable = false)
    private String direccion;

    private String telefono;

    @Email(message = "El formato del email no es válido")
    private String email;

    // Relación OneToMany con Inventario: una tienda puede tener muchos registros de inventario
    // mappedBy indica el campo en la entidad Inventario que posee la relación.
    // CascadeType.ALL: Operaciones (persist, merge, remove, refresh, detach) se propagarán.
    // orphanRemoval = true: Si un Inventario se desasocia de la Tienda, será eliminado.
    @OneToMany(mappedBy = "tienda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventario> inventarios = new ArrayList<>();
}