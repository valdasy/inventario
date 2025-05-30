package com.tralaleritos.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorSimpleResponseDTO {
    private Long id;
    private String nombre;
    // Puedes añadir más campos si los necesitas aquí, ej. private String email;
}