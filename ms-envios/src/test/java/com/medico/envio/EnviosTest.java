package com.medico.envio;

import com.medico.envio.client.PedidoClient;
import com.medico.envio.model.Envio;
import com.medico.envio.repository.EnvioRepository;
import com.medico.envio.service.EnvioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnviosTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private EnvioService envioService;

    private Envio envio1;
    private Envio envio2;

    @BeforeEach
    public void setup() {
        envio1 = new Envio();
        envio1.setId(1L);
        envio1.setPedidoId(100L);
        envio1.setDireccionEntrega("Calle Principal 123, Apt 4B");
        envio1.setCiudad("Santiago");
        envio1.setEstado("EN_TRANSITO");
        envio1.setFechaEnvio(LocalDate.now());
        envio1.setFechaEntregaEstimada(LocalDate.now().plusDays(3));
        envio1.setNumeroSeguimiento("TRACK123456789");

        envio2 = new Envio();
        envio2.setId(2L);
        envio2.setPedidoId(101L);
        envio2.setDireccionEntrega("Avenida Secundaria 456");
        envio2.setCiudad("Valparaíso");
        envio2.setEstado("ENTREGADO");
        envio2.setFechaEnvio(LocalDate.now().minusDays(5));
        envio2.setFechaEntregaEstimada(LocalDate.now().minusDays(2));
        envio2.setNumeroSeguimiento("TRACK987654321");
    }

    @Test
    public void crearEnvioExitosamente() {
        when(pedidoClient.existsById(100L)).thenReturn(true);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio1);

        Envio resultado = envioService.save(envio1);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getPedidoId());
        assertEquals("EN_TRANSITO", resultado.getEstado());
        assertEquals("TRACK123456789", resultado.getNumeroSeguimiento());
        verify(envioRepository, times(1)).save(envio1);
    }

    @Test
    public void crearEnvioSinPedidoDebeLanzarExcepcion() {
        Envio envioInvalido = new Envio();
        envioInvalido.setPedidoId(999L);
        when(pedidoClient.existsById(999L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> envioService.save(envioInvalido));

        assertEquals("No se puede crear el envío porque el pedido no existe", exception.getMessage());
        verify(pedidoClient).existsById(999L);
    }

    @Test
    public void encontrarEnvioPorId() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio1));

        Optional<Envio> resultado = envioRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Santiago", resultado.get().getCiudad());
        assertEquals("Calle Principal 123, Apt 4B", resultado.get().getDireccionEntrega());
    }

    @Test
    public void obtenerTodosLosEnvios() {
        List<Envio> envios = Arrays.asList(envio1, envio2);
        when(envioRepository.findAll()).thenReturn(envios);

        List<Envio> resultado = envioService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Santiago", resultado.get(0).getCiudad());
        assertEquals("Valparaíso", resultado.get(1).getCiudad());
    }

    @Test
    public void actualizarEstadoEnvio() {
        Envio envioActualizado = new Envio();
        envioActualizado.setId(1L);
        envioActualizado.setPedidoId(100L);
        envioActualizado.setDireccionEntrega("Calle Principal 123, Apt 4B");
        envioActualizado.setCiudad("Santiago");
        envioActualizado.setEstado("ENTREGADO");
        envioActualizado.setFechaEnvio(LocalDate.now());
        envioActualizado.setFechaEntregaEstimada(LocalDate.now().plusDays(3));
        envioActualizado.setNumeroSeguimiento("TRACK123456789");

        when(envioRepository.save(any(Envio.class))).thenReturn(envioActualizado);

        Envio resultado = envioService.update(envioActualizado);

        assertNotNull(resultado);
        assertEquals("ENTREGADO", resultado.getEstado());
    }

    @Test
    public void eliminarEnvio() {
        Long envioId = 1L;

        envioService.delete(envioId);

        verify(envioRepository, times(1)).deleteById(envioId);
    }

    @Test
    public void validarFormatoNumeroSeguimiento() {
        String numeroSeguimiento = envio1.getNumeroSeguimiento();

        assertTrue(numeroSeguimiento.matches("^TRACK[0-9]{9}$"),
            "Tracking number should start with TRACK followed by 9 digits");
    }

    @Test
    public void verificarFechasEnvio() {
        LocalDate fechaEnvio = envio1.getFechaEnvio();
        LocalDate fechaEntrega = envio1.getFechaEntregaEstimada();

        assertTrue(fechaEntrega.isAfter(fechaEnvio),
            "Estimated delivery date should be after shipment date");
    }

    @Test
    public void rechazarNumeroSeguimientoInvalido() {
        Envio envioInvalido = new Envio();
        envioInvalido.setNumeroSeguimiento("INVALID123");

        assertFalse(envioInvalido.getNumeroSeguimiento().matches("^TRACK[0-9]{9}$"));
    }

    @Test
    public void reconocerDireccionFaltante() {
        Envio envioInvalido = new Envio();
        envioInvalido.setDireccionEntrega(null);

        assertNull(envioInvalido.getDireccionEntrega());
    }

    @Test
    public void retornarVacioCuandoIdNoExiste() {
        Long idNoExistente = 99999L;
        when(envioRepository.findById(idNoExistente)).thenReturn(Optional.empty());

        Optional<Envio> resultado = envioRepository.findById(idNoExistente);

        assertFalse(resultado.isPresent());
    }

    @Test
    public void rechazarFechaEntregaAnterior() {
        Envio envioInvalido = new Envio();
        envioInvalido.setFechaEnvio(LocalDate.now().plusDays(5));
        envioInvalido.setFechaEntregaEstimada(LocalDate.now());

        assertFalse(envioInvalido.getFechaEntregaEstimada().isAfter(envioInvalido.getFechaEnvio()));
    }

    @Test
    public void rechazarEstadoNoSoportado() {
        Envio envioInvalido = new Envio();
        envioInvalido.setEstado("ESTADO_INVALIDO");

        String validStates = "EN_TRANSITO|ENTREGADO|CANCELADO|PENDIENTE";
        assertFalse(envioInvalido.getEstado().matches(validStates));
    }
}
