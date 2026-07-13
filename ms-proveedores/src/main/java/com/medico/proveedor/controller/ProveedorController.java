package com.medico.proveedor.controller;

import com.medico.proveedor.model.Proveedor;
import com.medico.proveedor.service.ProveedorService;
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
@RequestMapping("/api/v1/proveedores")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Proveedores", description = "Gestión de proveedores del sistema médico")
public class ProveedorController {
    private final ProveedorService service;

    @GetMapping
    @Operation(summary = "Obtener todos los proveedores", description = "Retorna una lista de todos los proveedores registrados")
    @ApiResponse(responseCode = "200", description = "Lista de proveedores obtenida exitosamente")
    public ResponseEntity<List<Proveedor>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    @Operation(summary = "Obtener proveedor por ID", description = "Retorna un proveedor específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<Proveedor> getById(
            @Parameter(description = "ID del proveedor", required = true)
            @PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new NoSuchElementException("Proveedor no encontrado con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo proveedor", description = "Registra un nuevo proveedor en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proveedor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Proveedor> create(
            @Parameter(description = "Datos del proveedor a crear")
            @RequestBody Proveedor entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @PutMapping("{id}")
    @Operation(summary = "Actualizar proveedor", description = "Actualiza un proveedor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Proveedor> update(
            @Parameter(description = "ID del proveedor a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del proveedor")
            @RequestBody Proveedor entity) {
        return service.findById(id).map(existing -> {
            entity.setId(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElseThrow(() -> new NoSuchElementException("Proveedor no encontrado con id " + id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Proveedor eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del proveedor a eliminar", required = true)
            @PathVariable Long id) {
        if(service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        throw new NoSuchElementException("Proveedor no encontrado con id " + id);
    }
}
