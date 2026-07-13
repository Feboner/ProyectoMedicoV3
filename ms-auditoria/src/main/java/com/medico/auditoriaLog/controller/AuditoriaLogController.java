package com.medico.auditoriaLog.controller;

import com.medico.auditoriaLog.model.AuditoriaLog;
import com.medico.auditoriaLog.service.AuditoriaLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/auditoria")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Auditoría", description = "Registros de auditoría y trazabilidad del sistema")
public class AuditoriaLogController {
    private final AuditoriaLogService service;

    @GetMapping
    @Operation(summary = "Obtener todos los registros de auditoría", description = "Retorna una lista de todos los registros de auditoría del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de registros obtenida exitosamente")
    public ResponseEntity<List<AuditoriaLog>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    @Operation(summary = "Obtener registro de auditoría por ID", description = "Retorna un registro específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    public ResponseEntity<AuditoriaLog> getById(
            @Parameter(description = "ID del registro de auditoría", required = true)
            @PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new NoSuchElementException("Registro de auditoría no encontrado con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo registro de auditoría", description = "Registra una nueva entrada en el log de auditoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<AuditoriaLog> create(
            @Parameter(description = "Datos del registro a crear")
            @RequestBody AuditoriaLog entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @PutMapping("{id}")
    @Operation(summary = "Actualizar registro de auditoría", description = "Actualiza un registro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<AuditoriaLog> update(
            @Parameter(description = "ID del registro a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del registro")
            @RequestBody AuditoriaLog entity) {
        return service.findById(id).map(existing -> {
            entity.setId(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElseThrow(() -> new NoSuchElementException("Registro de auditoría no encontrado con id " + id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar registro de auditoría", description = "Elimina un registro del log de auditoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del registro a eliminar", required = true)
            @PathVariable Long id) {
        if(service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        throw new NoSuchElementException("Registro de auditoría no encontrado con id " + id);
    }
}
