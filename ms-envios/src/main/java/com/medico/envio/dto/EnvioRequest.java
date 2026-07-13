package com.medico.envio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioRequest {
    private Long pedidoId;
    private String estado;
    private String direccion;
}
