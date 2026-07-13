package com.medico.pago;

import com.medico.pago.dto.PagoRequest;
import com.medico.pago.dto.PagoResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PagosTest {

    @Test
    void mapearSolicitudPago() {
        PagoRequest request = new PagoRequest(10L, 1500.50, "TRANSFERENCIA");
        PagoResponse response = new PagoResponse(1L, request.getPedidoId(), request.getMonto(), request.getMetodo());

        assertEquals(1L, response.getId());
        assertEquals(10L, response.getPedidoId());
        assertEquals(1500.50, response.getMonto());
        assertEquals("TRANSFERENCIA", response.getMetodo());
    }

    @Test
    void exigirMontoPago() {
        PagoRequest request = new PagoRequest(1L, null, "EFECTIVO");
        assertNull(request.getMonto());
    }
}
