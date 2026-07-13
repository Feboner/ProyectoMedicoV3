package com.duoc.ms_pacientes.repository;

import com.duoc.ms_pacientes.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    
    /**
     * Busca un paciente por su RUN
     * @param run RUN del paciente
     * @return Optional con el paciente si existe
     */
    Optional<Paciente> findByRun(String run);

    /**
     * Verifica si existe un paciente con el RUN especificado
     * @param run RUN del paciente
     * @return true si existe, false en caso contrario
     */
    boolean existsByRun(String run);

    /**
     * Verifica si existe un paciente con el correo especificado
     * @param correo correo del paciente
     * @return true si existe, false en caso contrario
     */
    boolean existsByCorreo(String correo);
}
