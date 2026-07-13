package com.medico.auditoriaLog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaLogResponse {
    private Long id;
    private String accion;
    private String detalle;
    private String usuario;
}
