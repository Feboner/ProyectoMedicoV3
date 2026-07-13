package com.duoc.ms_pacientes.controller;

import com.duoc.ms_pacientes.model.Paciente;
import com.duoc.ms_pacientes.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes", description = "Gestión de pacientes del sistema médico")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    /**
     * Listar todos los pacientes
     */
    @GetMapping
    @Operation(
            summary = "Obtener todos los pacientes",
            description = "Retorna la lista completa de pacientes registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de pacientes obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Paciente.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "No hay pacientes registrados"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<List<Paciente>> listar() {
        log.info("Listando todos los pacientes");
        List<Paciente> pacientes = pacienteService.getPacientes();

        if (pacientes.isEmpty()) {
            log.warn("No hay pacientes registrados en el sistema");
            return ResponseEntity.noContent().build();
        }

        log.info("Se encontraron {} pacientes", pacientes.size());
        return ResponseEntity.ok(pacientes);
    }

    /**
     * Obtener paciente por RUN
     */
    @GetMapping("/rut/{rut}")
    @Operation(
            summary = "Obtener paciente por RUN",
            description = "Busca y retorna un paciente específico usando su RUN (Run Único Nacional)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Paciente.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente con el RUN especificado no encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "RUN inválido o mal formado"
            )
    })
    public ResponseEntity<Paciente> obtenerPorRut(
            @Parameter(description = "RUN del paciente (sin dígito verificador)", example = "12345678", required = true)
            @PathVariable String rut) {
        log.info("Buscando paciente con RUN: {}", rut);

        Paciente paciente = pacienteService.obtenerPorRut(rut);

                if (paciente == null) {
                        log.warn("Paciente con RUN {} no encontrado", rut);
                        throw new NoSuchElementException("Paciente no encontrado con RUT " + rut);
                }

        log.info("Paciente encontrado: {} {}", paciente.getNombres(), paciente.getApellidos());
        return ResponseEntity.ok(paciente);
    }

    /**
     * Obtener paciente por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener paciente por ID",
            description = "Retorna los detalles de un paciente específico usando su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente encontrado",
                    content = @Content(schema = @Schema(implementation = Paciente.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrado"
            )
    })
    public ResponseEntity<Paciente> obtenerPorId(
            @Parameter(description = "ID del paciente", example = "1", required = true)
            @PathVariable Long id) {
        log.info("Buscando paciente con ID: {}", id);

        Paciente paciente = pacienteService.getPacienteId(id);

                if (paciente == null) {
                        log.warn("Paciente con ID {} no encontrado", id);
                        throw new NoSuchElementException("Paciente no encontrado con id " + id);
                }

        log.info("Paciente encontrado: {}", paciente.getNombres());
        return ResponseEntity.ok(paciente);
    }

    /**
     * Guardar nuevo paciente
     */
    @PostMapping
    @Operation(
            summary = "Crear nuevo paciente",
            description = "Registra un nuevo paciente en el sistema con sus datos básicos"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Paciente creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Paciente.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o campos requeridos faltantes"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto: RUN o correo ya existe en el sistema"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<?> guardar(
            @Valid @RequestBody Paciente paciente) {
        log.info("Creando nuevo paciente: {} {}", paciente.getNombres(), paciente.getApellidos());

        try {
            Paciente pacienteGuardado = pacienteService.savePaciente(paciente);
            log.info("Paciente creado exitosamente con ID: {}", pacienteGuardado.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(pacienteGuardado);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear paciente: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            error.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    /**
     * Actualizar paciente existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar paciente",
            description = "Actualiza los datos de un paciente existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Paciente.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            )
    })
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID del paciente a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody Paciente paciente) {
        log.info("Actualizando paciente con ID: {}", id);

        try {
            Paciente pacienteActualizado = pacienteService.updatePaciente(id, paciente);
            log.info("Paciente actualizado exitosamente");
            return ResponseEntity.ok(pacienteActualizado);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar paciente: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Eliminar paciente
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar paciente",
            description = "Elimina un paciente del sistema de forma permanente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente eliminado exitosamente",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error al eliminar paciente"
            )
    })
    public ResponseEntity<?> eliminar(
            @Parameter(description = "ID del paciente a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        log.info("Eliminando paciente con ID: {}", id);

        try {
            String mensaje = pacienteService.deletePaciente(id);
            log.info("Paciente eliminado: {}", mensaje);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", mensaje);
            response.put("id", id.toString());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar paciente: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
