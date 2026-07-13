package com.medico.producto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medico.producto.model.Producto;
import com.medico.producto.service.ProductoService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@DisplayName("Tests para ProductoController")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/productos - Obtener todos los productos")
    void testGetAllProductos() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Aspirina");
        producto.setDescripcion("Analgésico");
        producto.setPrecio(5.00);
        producto.setStock(10);

        when(productoService.findAll()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("Aspirina")));

        verify(productoService).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/productos/{id} - Obtener producto por ID")
    void testGetProductoById() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Aspirina");
        producto.setDescripcion("Analgésico");
        producto.setPrecio(5.00);
        producto.setStock(10);

        when(productoService.findById(1L)).thenReturn(Optional.of(producto));

        mockMvc.perform(get("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Aspirina")))
                .andExpect(jsonPath("$.precio", is(5.0)));

        verify(productoService).findById(1L);
    }

    @Test
    @DisplayName("POST /api/v1/productos - Crear nuevo producto")
    void testCreateProducto() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Aspirina");
        producto.setDescripcion("Analgésico");
        producto.setPrecio(5.00);
        producto.setStock(10);

        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/v1/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Aspirina")));

        verify(productoService).save(any(Producto.class));
    }

    @Test
    @DisplayName("PUT /api/v1/productos/{id} - Actualizar producto")
    void testUpdateProducto() throws Exception {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Aspirina");
        producto.setDescripcion("Analgésico");
        producto.setPrecio(5.00);
        producto.setStock(10);

        when(productoService.findById(1L)).thenReturn(Optional.of(new Producto()));
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Aspirina")));

        verify(productoService).findById(1L);
        verify(productoService).save(any(Producto.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/productos/{id} - Eliminar producto")
    void testDeleteProducto() throws Exception {
        when(productoService.findById(1L)).thenReturn(Optional.of(new Producto()));

        mockMvc.perform(delete("/api/v1/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productoService).findById(1L);
    }
}
