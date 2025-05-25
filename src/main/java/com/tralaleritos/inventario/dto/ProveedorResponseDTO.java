package com.tralaleritos.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorResponseDTO {

    private Long id;
    private String nombre;
    private String rut; // <-- ¡Asegúrate de que refleje tu cambio a RUT!
    private String direccion;
    private String telefono;
    private String email;
    // No incluimos la lista de productos suministrados aquí para evitar LazyInitializationException
    // y mantener la respuesta ligera. Si se necesitan, se puede crear otro DTO o un endpoint específico.
}