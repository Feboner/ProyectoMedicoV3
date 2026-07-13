package com.medico.paciente.controller;

import com.duoc.ms_pacientes.MsPacientesApplication;
import com.duoc.ms_pacientes.controller.PacienteController;
import com.duoc.ms_pacientes.model.Paciente;
import com.duoc.ms_pacientes.service.PacienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PacienteController.class)
@ContextConfiguration(classes = MsPacientesApplication.class)
@DisplayName("Tests para PacienteController")
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("GET /api/v1/pacientes - Obtener todos los pacientes")
    void testGetAllPacientes() throws Exception {
        when(pacienteService.getPacientes()).thenReturn(List.of(paciente));

        mockMvc.perform(get("/api/v1/pacientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].run", is("12345678")));

        verify(pacienteService).getPacientes();
    }

    @Test
    @DisplayName("GET /api/v1/pacientes/{id} - Obtener paciente por ID")
    void testGetPacienteById() throws Exception {
        when(pacienteService.getPacienteId(1L)).thenReturn(paciente);

        mockMvc.perform(get("/api/v1/pacientes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombres", is("Juan")));

        verify(pacienteService).getPacienteId(1L);
    }

    @Test
    @DisplayName("POST /api/v1/pacientes - Crear nuevo paciente")
    void testCreatePaciente() throws Exception {
        when(pacienteService.savePaciente(any(Paciente.class))).thenReturn(paciente);

        mockMvc.perform(post("/api/v1/pacientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.run", is("12345678")));

        verify(pacienteService).savePaciente(any(Paciente.class));
    }

    @Test
    @DisplayName("PUT /api/v1/pacientes/{id} - Actualizar paciente")
    void testUpdatePaciente() throws Exception {
        when(pacienteService.updatePaciente(eq(1L), any(Paciente.class))).thenReturn(paciente);

        mockMvc.perform(put("/api/v1/pacientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombres", is("Juan")));

        verify(pacienteService).updatePaciente(eq(1L), any(Paciente.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/pacientes/{id} - Eliminar paciente")
    void testDeletePaciente() throws Exception {
        when(pacienteService.deletePaciente(1L)).thenReturn("Paciente eliminado exitosamente");

        mockMvc.perform(delete("/api/v1/pacientes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje", is("Paciente eliminado exitosamente")));

        verify(pacienteService).deletePaciente(1L);
    }
}
