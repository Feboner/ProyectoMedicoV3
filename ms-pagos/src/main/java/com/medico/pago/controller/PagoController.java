package com.medico.pago.controller;

import com.medico.pago.model.Pago;
import com.medico.pago.service.PagoService;
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
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pagos", description = "Gestión de pagos médicos")
public class PagoController {
    private final PagoService service;

    @GetMapping
    @Operation(summary = "Obtener todos los pagos", description = "Retorna una lista de todos los pagos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de pagos obtenida exitosamente")
    public ResponseEntity<List<Pago>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    @Operation(summary = "Obtener pago por ID", description = "Retorna un pago específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Pago> getById(
            @Parameter(description = "ID del pago", required = true)
            @PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new NoSuchElementException("Pago no encontrado con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo pago", description = "Registra un nuevo pago en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pago creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Pago> create(
            @Parameter(description = "Datos del pago a crear")
            @RequestBody Pago entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @PutMapping("{id}")
    @Operation(summary = "Actualizar pago", description = "Actualiza un pago existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pago actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Pago> update(
            @Parameter(description = "ID del pago a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del pago")
            @RequestBody Pago entity) {
        return service.findById(id).map(existing -> {
            entity.setId(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElseThrow(() -> new NoSuchElementException("Pago no encontrado con id " + id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar pago", description = "Elimina un pago del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del pago a eliminar", required = true)
            @PathVariable Long id) {
        if(service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        throw new NoSuchElementException("Pago no encontrado con id " + id);
    }
}
