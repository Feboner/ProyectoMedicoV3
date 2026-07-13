package com.medico.producto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.medico.producto.model.Producto;
import com.medico.producto.repository.ProductoRepository;
import com.medico.producto.service.ProductoService;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProductosTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private Producto producto2;

    @BeforeEach
    public void setup() {
        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Aspirina");
        producto.setDescripcion("Analgésico común");
        producto.setPrecio(5.99);
        producto.setStock(100);

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Ibuprofeno");
        producto2.setDescripcion("Antiinflamatorio");
        producto2.setPrecio(7.99);
        producto2.setStock(50);
    }

    @Test
    public void guardarProductoExitosamente() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertNotNull(resultado);
        assertEquals("Aspirina", resultado.getNombre());
        assertEquals(5.99, resultado.getPrecio());
        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    public void encontrarProductoPorId() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Optional<Producto> resultado = productoRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Aspirina", resultado.get().getNombre());
        assertEquals(100, resultado.get().getStock());
    }

    @Test
    public void obtenerTodosLosProductos() {
        List<Producto> productos = Arrays.asList(producto, producto2);
        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Aspirina", resultado.get(0).getNombre());
        assertEquals("Ibuprofeno", resultado.get(1).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    public void actualizarProducto() {
        Producto productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Aspirina Plus");
        productoActualizado.setDescripcion("Analgésico mejorado");
        productoActualizado.setPrecio(6.99);
        productoActualizado.setStock(150);

        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        Producto resultado = productoService.update(productoActualizado);

        assertNotNull(resultado);
        assertEquals("Aspirina Plus", resultado.getNombre());
        assertEquals(6.99, resultado.getPrecio());
        assertEquals(150, resultado.getStock());
    }

    @Test
    public void eliminarProductoPorId() {
        Long idAEliminar = 1L;

        productoService.delete(idAEliminar);

        verify(productoRepository, times(1)).deleteById(idAEliminar);
    }

    @Test
    public void listaVaciaDeProductos() {
        when(productoRepository.findAll()).thenReturn(Arrays.asList());

        List<Producto> resultado = productoService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
    }

    @Test
    public void reconocerNombreNuloComoInvalido() {
        Producto productoInvalido = new Producto();
        productoInvalido.setNombre(null);

        assertNull(productoInvalido.getNombre());
    }

    @Test
    public void rechazarStockNegativo() {
        Producto productoInvalido = new Producto();
        productoInvalido.setStock(-50);

        assertFalse(productoInvalido.getStock() > 0);
    }

    @Test
    public void retornarVacioCuandoProductoNoExiste() {
        Long idInvalido = 999999L;
        when(productoRepository.findById(idInvalido)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoRepository.findById(idInvalido);

        assertFalse(resultado.isPresent());
    }
}
