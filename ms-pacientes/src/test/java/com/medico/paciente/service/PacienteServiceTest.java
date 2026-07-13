package com.medico.paciente.service;

import com.duoc.ms_pacientes.model.Paciente;
import com.duoc.ms_pacientes.repository.PacienteRepository;
import com.duoc.ms_pacientes.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para PacienteService")
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1);
        paciente.setRun("12345678");
        paciente.setNombres("Juan");
        paciente.setApellidos("Pérez");
        paciente.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        paciente.setCorreo("juan@test.com");
        paciente.setCreatedAt(LocalDate.now());
    }

    @Test
    @DisplayName("Obtener todos los pacientes - éxito")
    void testGetPacientes() {
        when(pacienteRepository.findAll()).thenReturn(List.of(paciente));

        List<Paciente> result = pacienteService.getPacientes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombres());
        verify(pacienteRepository).findAll();
    }

    @Test
    @DisplayName("Obtener paciente por ID - éxito")
    void testGetPacienteId() {
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        Paciente result = pacienteService.getPacienteId(1L);

        assertNotNull(result);
        assertEquals("Juan", result.getNombres());
        verify(pacienteRepository).findById(1);
    }

    @Test
    @DisplayName("Crear paciente - éxito")
    void testSavePaciente() {
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        Paciente result = pacienteService.savePaciente(paciente);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Eliminar paciente - éxito")
    void testDeletePaciente() {
        when(pacienteRepository.existsById(1)).thenReturn(true);
        doNothing().when(pacienteRepository).deleteById(1);

        String result = pacienteService.deletePaciente(1L);

        assertEquals("Paciente eliminado exitosamente", result);
        verify(pacienteRepository).deleteById(1);
    }
}
