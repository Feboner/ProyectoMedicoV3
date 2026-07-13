package com.medico.proveedor;

import com.medico.proveedor.dto.ProveedorRequest;
import com.medico.proveedor.dto.ProveedorResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProveedoresTest {

    @Test
    void mapearDtoSolicitudADtoRespuesta() {
        ProveedorRequest request = new ProveedorRequest("Laboratorios SA", "Ana", "987654321");

        ProveedorResponse response = new ProveedorResponse(1L, request.getNombre(), request.getContacto(), request.getTelefono());

        assertEquals(1L, response.getId());
        assertEquals("Laboratorios SA", response.getNombre());
        assertEquals("Ana", response.getContacto());
        assertEquals("987654321", response.getTelefono());
    }

    @Test
    void rechazarCamposObligatoriosVaciosEnDto() {
        ProveedorRequest request = new ProveedorRequest("", "", "");

        assertTrue(request.getNombre().isBlank());
        assertTrue(request.getContacto().isBlank());
        assertTrue(request.getTelefono().isBlank());
    }
}
