package com.tralaleritos.inventario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiendaRequestDTO {

    @NotBlank(message = "El nombre de la tienda no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La dirección de la tienda no puede estar vacío")
    private String direccion;

    private String telefono;

    @Email(message = "El formato del email no es válido")
    private String email;
}