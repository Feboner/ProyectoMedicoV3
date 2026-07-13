package com.medico.receta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaResponse {
    private Long id;
    private Long pacienteId;
    private String medicamento;
    private String indicaciones;
}
