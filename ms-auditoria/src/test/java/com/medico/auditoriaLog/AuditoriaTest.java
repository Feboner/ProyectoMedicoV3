package com.medico.auditoriaLog;

import com.medico.auditoriaLog.dto.AuditoriaLogRequest;
import com.medico.auditoriaLog.dto.AuditoriaLogResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuditoriaTest {

    @Test
    void mapearSolicitudAuditoria() {
        AuditoriaLogRequest request = new AuditoriaLogRequest("LOGIN", "Usuario autenticado", "admin");
        AuditoriaLogResponse response = new AuditoriaLogResponse(1L, request.getAccion(), request.getDetalle(), request.getUsuario());

        assertEquals(1L, response.getId());
        assertEquals("LOGIN", response.getAccion());
        assertEquals("Usuario autenticado", response.getDetalle());
        assertEquals("admin", response.getUsuario());
    }

    @Test
    void conservarValorAccion() {
        AuditoriaLogRequest request = new AuditoriaLogRequest("LOGOUT", "Cierre de sesión", "admin");
        assertEquals("LOGOUT", request.getAccion());
    }
}
