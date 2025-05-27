package com.tralaleritos.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiendaResponseDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;

    // --- NUEVOS ATRIBUTOS ---
    private String horariosApertura;
    private String personalAsignado;
    private String politicasLocales;
}