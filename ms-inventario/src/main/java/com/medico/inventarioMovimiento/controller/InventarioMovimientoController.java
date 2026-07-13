package com.medico.inventarioMovimiento.controller;

import com.medico.inventarioMovimiento.model.InventarioMovimiento;
import com.medico.inventarioMovimiento.service.InventarioMovimientoService;
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
@RequestMapping("/api/v1/inventario")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inventario", description = "Gestión de movimientos de inventario")
public class InventarioMovimientoController {
    private final InventarioMovimientoService service;

    @GetMapping
    @Operation(summary = "Obtener todos los movimientos de inventario", description = "Retorna una lista de todos los movimientos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida exitosamente")
    public ResponseEntity<List<InventarioMovimiento>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    @Operation(summary = "Obtener movimiento por ID", description = "Retorna un movimiento específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    public ResponseEntity<InventarioMovimiento> getById(
            @Parameter(description = "ID del movimiento", required = true)
            @PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new NoSuchElementException("Movimiento de inventario no encontrado con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo movimiento de inventario", description = "Registra un nuevo movimiento en el inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<InventarioMovimiento> create(
            @Parameter(description = "Datos del movimiento a crear")
            @RequestBody InventarioMovimiento entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @PutMapping("{id}")
    @Operation(summary = "Actualizar movimiento", description = "Actualiza un movimiento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<InventarioMovimiento> update(
            @Parameter(description = "ID del movimiento a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del movimiento")
            @RequestBody InventarioMovimiento entity) {
        return service.findById(id).map(existing -> {
            entity.setId(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElseThrow(() -> new NoSuchElementException("Movimiento de inventario no encontrado con id " + id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar movimiento", description = "Elimina un movimiento del inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del movimiento a eliminar", required = true)
            @PathVariable Long id) {
        if(service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        throw new NoSuchElementException("Movimiento de inventario no encontrado con id " + id);
    }
}