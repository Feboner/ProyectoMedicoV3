package com.medico.receta.controller;

import com.medico.receta.model.Receta;
import com.medico.receta.service.RecetaService;
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
@RequestMapping("/api/v1/recetas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recetas", description = "Gestión de recetas médicas")
public class RecetaController {
    private final RecetaService service;

    @GetMapping
    @Operation(summary = "Obtener todas las recetas", description = "Retorna una lista de todas las recetas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de recetas obtenida exitosamente")
    public ResponseEntity<List<Receta>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    @Operation(summary = "Obtener receta por ID", description = "Retorna una receta específica por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receta encontrada"),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    })
    public ResponseEntity<Receta> getById(
            @Parameter(description = "ID de la receta", required = true)
            @PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new NoSuchElementException("Receta no encontrada con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva receta", description = "Crea una nueva receta médica en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Receta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Receta> create(
            @Parameter(description = "Datos de la receta a crear")
            @RequestBody Receta entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @PutMapping("{id}")
    @Operation(summary = "Actualizar receta", description = "Actualiza una receta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Receta actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Receta> update(
            @Parameter(description = "ID de la receta a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos de la receta")
            @RequestBody Receta entity) {
        return service.findById(id).map(existing -> {
            entity.setId(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElseThrow(() -> new NoSuchElementException("Receta no encontrada con id " + id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar receta", description = "Elimina una receta del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Receta eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la receta a eliminar", required = true)
            @PathVariable Long id) {
        if(service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        throw new NoSuchElementException("Receta no encontrada con id " + id);
    }
}
