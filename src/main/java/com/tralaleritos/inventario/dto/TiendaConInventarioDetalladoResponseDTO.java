package com.tralaleritos.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List; // Asegúrate de tener este import

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiendaConInventarioDetalladoResponseDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String horariosApertura;
    private String personalAsignado;
    private String politicasLocales;
    private List<InventarioDetalladoResponseDTO> inventario; // Lista del inventario detallado
}