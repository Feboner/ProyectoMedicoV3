package com.medico.pedido.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medico.pedido.model.Pedido;
import com.medico.pedido.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

@WebMvcTest(PedidoController.class)
@DisplayName("Tests para PedidoController")
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/pedidos - Obtener todos los pedidos")
    void testGetAllPedidos() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setPacienteId(10L);
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(25000.0);

        when(pedidoService.findAll()).thenReturn(List.of(pedido));

        mockMvc.perform(get("/api/v1/pedidos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].estado", is("PENDIENTE")));

        verify(pedidoService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/pedidos/{id} - Obtener pedido por ID")
    void testGetPedidoById() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setPacienteId(10L);
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(25000.0);

        when(pedidoService.findById(1L)).thenReturn(Optional.of(pedido));

        mockMvc.perform(get("/api/v1/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.estado", is("PENDIENTE")));

        verify(pedidoService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("POST /api/v1/pedidos - Crear nuevo pedido")
    void testCreatePedido() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setPacienteId(10L);
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(25000.0);

        when(pedidoService.save(any(Pedido.class))).thenReturn(pedido);

        mockMvc.perform(post("/api/v1/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.estado", is("PENDIENTE")));

        verify(pedidoService, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/pedidos/{id} - Eliminar pedido")
    void testDeletePedido() throws Exception {
        when(pedidoService.findById(1L)).thenReturn(Optional.of(new Pedido()));
        doNothing().when(pedidoService).delete(1L);

        mockMvc.perform(delete("/api/v1/pedidos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(pedidoService, times(1)).delete(1L);
    }
}
