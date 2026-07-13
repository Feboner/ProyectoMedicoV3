package com.medico.pago.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {
    private Long id;
    private Long pedidoId;
    private Double monto;
    private String metodo;
}
