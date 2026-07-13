package com.medico.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medico.inventarioMovimiento.InventarioApplication;
import com.medico.inventarioMovimiento.controller.InventarioMovimientoController;
import com.medico.inventarioMovimiento.model.InventarioMovimiento;
import com.medico.inventarioMovimiento.service.InventarioMovimientoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventarioMovimientoController.class)
@ContextConfiguration(classes = InventarioApplication.class)
@DisplayName("Tests para InventarioMovimientoController")
class InventarioMovimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioMovimientoService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/inventario - Obtener todos los movimientos")
    void testGetAllMovimientos() throws Exception {
        InventarioMovimiento movimiento = new InventarioMovimiento();
        movimiento.setId(1L);

        when(inventarioService.findAll()).thenReturn(List.of(movimiento));

        mockMvc.perform(get("/api/v1/inventario")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(inventarioService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/inventario/{id} - Obtener movimiento por ID")
    void testGetMovimientoById() throws Exception {
        InventarioMovimiento movimiento = new InventarioMovimiento();
        movimiento.setId(1L);

        when(inventarioService.findById(1L)).thenReturn(Optional.of(movimiento));

        mockMvc.perform(get("/api/v1/inventario/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(inventarioService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("POST /api/v1/inventario - Crear nuevo movimiento")
    void testCreateMovimiento() throws Exception {
        InventarioMovimiento movimiento = new InventarioMovimiento();
        movimiento.setId(1L);

        when(inventarioService.save(any(InventarioMovimiento.class))).thenReturn(movimiento);

        mockMvc.perform(post("/api/v1/inventario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movimiento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)));

        verify(inventarioService, times(1)).save(any(InventarioMovimiento.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/inventario/{id} - Eliminar movimiento")
    void testDeleteMovimiento() throws Exception {
        when(inventarioService.findById(1L)).thenReturn(Optional.of(new InventarioMovimiento()));
        doNothing().when(inventarioService).delete(1L);

        mockMvc.perform(delete("/api/v1/inventario/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(inventarioService, times(1)).delete(1L);
    }
}
