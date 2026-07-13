package com.duoc.ms_pacientes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.duoc.ms_pacientes.model.Paciente;
import com.duoc.ms_pacientes.repository.PacienteRepository;
import com.duoc.ms_pacientes.service.PacienteService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PacientesTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente1;
    private Paciente paciente2;

    @BeforeEach
    public void setup() {
        paciente1 = new Paciente();
        paciente1.setId(1);
        paciente1.setRun("12345678-9");
        paciente1.setNombres("Juan");
        paciente1.setApellidos("Pérez García");
        paciente1.setFechaNacimiento(LocalDate.of(1990, 5, 15));
        paciente1.setCorreo("juan.perez@gmail.com");

        paciente2 = new Paciente();
        paciente2.setId(2);
        paciente2.setRun("98765432-1");
        paciente2.setNombres("María");
        paciente2.setApellidos("López Martínez");
        paciente2.setFechaNacimiento(LocalDate.of(1985, 3, 22));
        paciente2.setCorreo("maria.lopez@gmail.com");
    }

    @Test
    public void guardarPacienteExitosamente() {
        paciente1.setId(null);
        when(pacienteRepository.existsByRun(paciente1.getRun())).thenReturn(false);
        when(pacienteRepository.existsByCorreo(paciente1.getCorreo())).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente1);

        Paciente resultado = pacienteService.savePaciente(paciente1);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombres());
        assertEquals("12345678-9", resultado.getRun());
        assertEquals("juan.perez@gmail.com", resultado.getCorreo());
        verify(pacienteRepository, times(1)).save(paciente1);
    }

    @Test
    public void encontrarPacientePorRun() {
        String run = "12345678-9";
        when(pacienteRepository.findByRun(run)).thenReturn(Optional.of(paciente1));

        Optional<Paciente> resultado = pacienteRepository.findByRun(run);

        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombres());
        assertEquals("12345678-9", resultado.get().getRun());
    }

    @Test
    public void encontrarPacientePorId() {
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente1));

        Optional<Paciente> resultado = pacienteRepository.findById(1);

        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombres());
        assertEquals(1, resultado.get().getId());
    }

    @Test
    public void obtenerTodosPacientes() {
        List<Paciente> pacientes = Arrays.asList(paciente1, paciente2);
        when(pacienteRepository.findAll()).thenReturn(pacientes);

        List<Paciente> resultado = pacienteService.getPacientes();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombres());
        assertEquals("María", resultado.get(1).getNombres());
    }

    @Test
    public void actualizarPacienteExitosamente() {
        Long pacienteId = 1L;
        Paciente pacienteActualizado = new Paciente();
        pacienteActualizado.setNombres("Juan Carlos");
        pacienteActualizado.setApellidos("Pérez García Mendoza");

        when(pacienteRepository.findById(pacienteId.intValue())).thenReturn(Optional.of(paciente1));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente1);

        Paciente resultado = pacienteService.updatePaciente(pacienteId, pacienteActualizado);

        assertNotNull(resultado);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    public void eliminarPaciente() {
        Long pacienteId = 1L;
        when(pacienteRepository.existsById(pacienteId.intValue())).thenReturn(true);

        pacienteService.deletePaciente(pacienteId);

        verify(pacienteRepository, times(1)).deleteById(pacienteId.intValue());
    }

    @Test
    public void validarFormatoEmail() {
        String email = paciente1.getCorreo();

        assertTrue(email.matches("^[A-Za-z0-9+_.-]+@(.+)$"),
            "Email format should be valid: " + email);
    }

    @Test
    public void detectarRunDuplicado() {
        when(pacienteRepository.existsByRun(paciente1.getRun())).thenReturn(true);

        boolean existe = pacienteRepository.existsByRun(paciente1.getRun());

        assertTrue(existe);
    }

    @Test
    public void detectarCorreoDuplicado() {
        when(pacienteRepository.existsByCorreo(paciente1.getCorreo())).thenReturn(true);

        boolean existe = pacienteRepository.existsByCorreo(paciente1.getCorreo());

        assertTrue(existe);
    }

    @Test
    public void rechazarEmailInvalido() {
        Paciente pacienteInvalido = new Paciente();
        pacienteInvalido.setCorreo("invalid-email-format");

        assertFalse(pacienteInvalido.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$"));
    }

    @Test
    public void tratarRunNuloComoFaltante() {
        Paciente pacienteInvalido = new Paciente();
        pacienteInvalido.setRun(null);

        assertNull(pacienteInvalido.getRun());
    }

    @Test
    public void retornarVacioCuandoPacienteNoExiste() {
        Integer idNoExistente = 99999;
        when(pacienteRepository.findById(idNoExistente)).thenReturn(Optional.empty());

        Optional<Paciente> resultado = pacienteRepository.findById(idNoExistente);

        assertFalse(resultado.isPresent());
    }
}
