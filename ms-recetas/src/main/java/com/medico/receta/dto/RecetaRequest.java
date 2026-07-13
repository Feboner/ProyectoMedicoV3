package com.medico.receta.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecetaRequest {
    private Long pacienteId;
    private String medicamento;
    private String indicaciones;
}
