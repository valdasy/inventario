package com.tralaleritos.inventario.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore; // <<--- IMPORTANTE
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@Table(name = "proveedores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@ToString(exclude = "productosSuministrados")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del proveedor no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El RUT del proveedor no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String rut;

    private String direccion;

    private String telefono;

    @NotBlank(message = "El email del proveedor no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // <--- USAREMOS ESTO AQUÍ
    private List<Producto> productosSuministrados = new ArrayList<>();

    public Proveedor(String nombre, String rut, String direccion, String telefono, String email) {
        this.nombre = nombre;
        this.rut = rut;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }
}