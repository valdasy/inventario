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
@Table(name = "tiendas")
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

    // --- NUEVOS ATRIBUTOS ---

    // Horarios de apertura (ej. "L-V: 9am-6pm; S: 10am-2pm")
    @Column(columnDefinition = "TEXT") // Puede ser un texto largo
    private String horariosApertura;

    // Personal asignado (ej. "Gerente: Juan Perez, Vendedor: Maria Lopez")
    // Podría ser un JSON string si quieres estructura {"gerente": "Juan Perez"}
    @Column(columnDefinition = "TEXT")
    private String personalAsignado;

    // Políticas locales (ej. "Política de devoluciones: 30 días con boleta")
    @Column(columnDefinition = "TEXT")
    private String politicasLocales;

    // Relación OneToMany con Inventario: una tienda puede tener muchos registros de inventario
    @OneToMany(mappedBy = "tienda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventario> inventarios = new ArrayList<>();
}