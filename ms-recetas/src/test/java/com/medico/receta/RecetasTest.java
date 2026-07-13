package com.medico.receta;

import com.medico.receta.dto.RecetaRequest;
import com.medico.receta.dto.RecetaResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecetasTest {

    @Test
    void mapearSolicitudReceta() {
        RecetaRequest request = new RecetaRequest(5L, "Paracetamol", "Cada 8 horas");
        RecetaResponse response = new RecetaResponse(1L, request.getPacienteId(), request.getMedicamento(), request.getIndicaciones());

        assertEquals(1L, response.getId());
        assertEquals(5L, response.getPacienteId());
        assertEquals("Paracetamol", response.getMedicamento());
        assertEquals("Cada 8 horas", response.getIndicaciones());
    }

    @Test
    void conservarNombreMedicamentoEnDto() {
        RecetaRequest request = new RecetaRequest(7L, "Ibuprofeno", "Despues de comer");
        assertEquals("Ibuprofeno", request.getMedicamento());
    }
}
