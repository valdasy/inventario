package com.tralaleritos.inventario.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // O @JsonManagedReference
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Si decides usarla para la relación con Inventario
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tiendas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString(exclude = "inventarios")
public class Tienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la tienda no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotBlank(message = "La dirección de la tienda no puede estar vacía")
    @Column(nullable = false)
    private String direccion;

    private String telefono;

    @Email(message = "El formato del email no es válido")
    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String horariosApertura;

    @Column(columnDefinition = "TEXT")
    private String personalAsignado;

    @Column(columnDefinition = "TEXT")
    private String politicasLocales;

    @OneToMany(mappedBy = "tienda", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // Elige una estrategia para la relación Tienda <-> Inventario:
    // Opción A: Ignorar y tener endpoint separado (consistente con Categoria y Proveedor)
    @JsonIgnore
    // Opción B: Usar Managed/Back Reference
    // @JsonManagedReference("tienda-inventarios")
    private List<Inventario> inventarios = new ArrayList<>();

    public Tienda(String nombre, String direccion, String telefono, String email, String horariosApertura, String personalAsignado, String politicasLocales) {
        // ... constructor ...
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.horariosApertura = horariosApertura;
        this.personalAsignado = personalAsignado;
        this.politicasLocales = politicasLocales;
    }
}