package com.duoc.ms_pacientes.service;

import com.duoc.ms_pacientes.model.Paciente;
import com.duoc.ms_pacientes.repository.PacienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    public List<Paciente> getPacientes() {
        log.info("Obteniendo lista de todos los pacientes");
        return pacienteRepository.findAll();
    }

    public Paciente savePaciente(Paciente paciente) {
        log.info("Guardando paciente con RUN: {}", paciente.getRun());

        if (paciente.getId() == null && pacienteRepository.existsByRun(paciente.getRun())) {
            log.warn("Intento de crear paciente con RUN duplicado: {}", paciente.getRun());
            throw new IllegalArgumentException("Ya existe un paciente con el RUN: " + paciente.getRun());
        }

        if (paciente.getId() == null && pacienteRepository.existsByCorreo(paciente.getCorreo())) {
            log.warn("Intento de crear paciente con correo duplicado: {}", paciente.getCorreo());
            throw new IllegalArgumentException("Ya existe un paciente con el correo: " + paciente.getCorreo());
        }

        Paciente savedPaciente = pacienteRepository.save(paciente);
        log.info("Paciente guardado exitosamente con ID: {}", savedPaciente.getId());
        return savedPaciente;
    }

    public Paciente getPacienteId(Long id) {
        log.info("Buscando paciente con ID: {}", id);
        Optional<Paciente> paciente = pacienteRepository.findById(id.intValue());
        return paciente.orElse(null);
    }

    public Paciente obtenerPorRut(String run) {
        log.info("Buscando paciente con RUN: {}", run);
        Optional<Paciente> paciente = pacienteRepository.findByRun(run);
        return paciente.orElse(null);
    }

    public String deletePaciente(Long id) {
        log.info("Eliminando paciente con ID: {}", id);
        
        if (!pacienteRepository.existsById(id.intValue())) {
            log.warn("Intento de eliminar paciente inexistente con ID: {}", id);
            throw new IllegalArgumentException("Paciente con ID " + id + " no existe");
        }

        pacienteRepository.deleteById(id.intValue());
        log.info("Paciente con ID {} eliminado exitosamente", id);
        return "Paciente eliminado exitosamente";
    }

    public Paciente updatePaciente(Long id, Paciente pacienteActualizado) {
        log.info("Actualizando paciente con ID: {}", id);
        
        Optional<Paciente> pacienteExistente = pacienteRepository.findById(id.intValue());
        if (pacienteExistente.isEmpty()) {
            log.warn("Intento de actualizar paciente inexistente con ID: {}", id);
            throw new IllegalArgumentException("Paciente con ID " + id + " no existe");
        }

        Paciente paciente = pacienteExistente.get();

        if (pacienteActualizado.getNombres() != null && !pacienteActualizado.getNombres().isBlank()) {
            paciente.setNombres(pacienteActualizado.getNombres());
        }
        if (pacienteActualizado.getApellidos() != null && !pacienteActualizado.getApellidos().isBlank()) {
            paciente.setApellidos(pacienteActualizado.getApellidos());
        }
        if (pacienteActualizado.getFechaNacimiento() != null) {
            paciente.setFechaNacimiento(pacienteActualizado.getFechaNacimiento());
        }
        if (pacienteActualizado.getCorreo() != null && !pacienteActualizado.getCorreo().isBlank()) {
            paciente.setCorreo(pacienteActualizado.getCorreo());
        }

        Paciente updatedPaciente = pacienteRepository.save(paciente);
        log.info("Paciente con ID {} actualizado exitosamente", id);
        return updatedPaciente;
    }
}
