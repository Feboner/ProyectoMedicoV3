package com.duoc.ms_pacientes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteResponse {
    private Integer id;
    private String run;
    private String nombres;
    private String apellidos;
    private String correo;
}
