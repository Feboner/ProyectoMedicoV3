package com.medico.producto.service;

import com.medico.producto.model.Producto;
import com.medico.producto.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para ProductoService")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Aspirina");
        producto.setDescripcion("Analgésico");
        producto.setPrecio(5.00);
    }

    @Test
    @DisplayName("Obtener todos los productos - éxito")
    void testObtenerTodos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));

        List<Producto> result = productoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aspirina", result.get(0).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener producto por ID - éxito")
    void testObtenerPorId() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> result = productoService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Aspirina", result.get().getNombre());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Obtener producto por ID - no encontrado")
    void testObtenerPorIdNotFound() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> result = productoService.findById(99L);

        assertFalse(result.isPresent());
        verify(productoRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Crear producto - éxito")
    void testCrear() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.save(producto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Aspirina", result.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("Actualizar producto - éxito")
    void testActualizar() {
        producto.setNombre("Aspirina 500mg");
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.update(producto);

        assertNotNull(result);
        assertEquals("Aspirina 500mg", result.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("Eliminar producto - éxito")
    void testEliminar() {
        doNothing().when(productoRepository).deleteById(1L);

        productoService.delete(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }
}
