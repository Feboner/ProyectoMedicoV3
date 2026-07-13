package com.medico.inventarioMovimiento;

import com.medico.inventarioMovimiento.dto.InventarioMovimientoRequest;
import com.medico.inventarioMovimiento.dto.InventarioMovimientoResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventarioTest {

    @Test
    void mapearSolicitudMovimiento() {
        InventarioMovimientoRequest request = new InventarioMovimientoRequest(100L, 10, "ENTRADA");
        InventarioMovimientoResponse response = new InventarioMovimientoResponse(1L, request.getProductoId(), request.getCantidad(), request.getTipo());

        assertEquals(1L, response.getId());
        assertEquals(100L, response.getProductoId());
        assertEquals(10, response.getCantidad());
        assertEquals("ENTRADA", response.getTipo());
    }

    @Test
    void conservarTipoMovimiento() {
        InventarioMovimientoRequest request = new InventarioMovimientoRequest(200L, -3, "SALIDA");
        assertEquals("SALIDA", request.getTipo());
    }
}
