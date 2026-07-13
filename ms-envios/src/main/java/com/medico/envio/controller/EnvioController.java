package com.medico.envio.controller;

import com.medico.envio.model.Envio;
import com.medico.envio.service.EnvioService;
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
@RequestMapping("/api/v1/envios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Envíos", description = "Gestión de envíos y distribución")
public class EnvioController {
    private final EnvioService service;

    @GetMapping
    @Operation(summary = "Obtener todos los envíos", description = "Retorna una lista de todos los envíos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de envíos obtenida exitosamente")
    public ResponseEntity<List<Envio>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    @Operation(summary = "Obtener envío por ID", description = "Retorna un envío específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envío encontrado"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public ResponseEntity<Envio> getById(
            @Parameter(description = "ID del envío", required = true)
            @PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new NoSuchElementException("Envio no encontrado con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo envío", description = "Crea un nuevo registro de envío")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Envío creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Envio> create(
            @Parameter(description = "Datos del envío a crear")
            @RequestBody Envio entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @PutMapping("{id}")
    @Operation(summary = "Actualizar envío", description = "Actualiza un envío existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Envío actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Envio> update(
            @Parameter(description = "ID del envío a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del envío")
            @RequestBody Envio entity) {
        return service.findById(id).map(existing -> {
            entity.setId(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElseThrow(() -> new NoSuchElementException("Envio no encontrado con id " + id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar envío", description = "Elimina un envío del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Envío eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del envío a eliminar", required = true)
            @PathVariable Long id) {
        if(service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        throw new NoSuchElementException("Envio no encontrado con id " + id);
    }
}
