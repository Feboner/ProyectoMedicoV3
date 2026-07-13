package com.medico.pedido;

import com.medico.pedido.dto.PedidoRequest;
import com.medico.pedido.dto.PedidoResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PedidosTest {

    @Test
    void mapearSolicitudPedido() {
        PedidoRequest request = new PedidoRequest(88L, "PENDIENTE", 25000.0);
        PedidoResponse response = new PedidoResponse(1L, request.getPacienteId(), request.getEstado(), request.getTotal());

        assertEquals(1L, response.getId());
        assertEquals(88L, response.getPacienteId());
        assertEquals("PENDIENTE", response.getEstado());
        assertEquals(25000.0, response.getTotal());
    }

    @Test
    void manejarEstadoPendientePedido() {
        PedidoRequest request = new PedidoRequest(2L, "PENDIENTE", 5000.0);
        assertEquals("PENDIENTE", request.getEstado());
    }
}
