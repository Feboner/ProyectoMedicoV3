package com.medico.proveedor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorRequest {
    private String nombre;
    private String contacto;
    private String telefono;
}
