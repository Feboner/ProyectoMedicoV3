package com.medico.producto.controller;

import com.medico.producto.model.Producto;
import com.medico.producto.service.ProductoService;
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
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Productos", description = "Gestión de productos del sistema médico")
public class ProductoController {
    private final ProductoService service;

    @GetMapping
    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    public ResponseEntity<List<Producto>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna un producto específico por su identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Producto> getById(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElseThrow(() -> new NoSuchElementException("Producto no encontrado con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Producto> create(
            @Parameter(description = "Datos del producto a crear")
            @RequestBody Producto entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @PutMapping("{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<Producto> update(
            @Parameter(description = "ID del producto a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del producto")
            @RequestBody Producto entity) {
        return service.findById(id).map(existing -> {
            entity.setId(id);
            return ResponseEntity.ok(service.save(entity));
        }).orElseThrow(() -> new NoSuchElementException("Producto no encontrado con id " + id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del producto a eliminar", required = true)
            @PathVariable Long id) {
        if(service.findById(id).isPresent()) {
            service.delete(id);
            return ResponseEntity.noContent().build();
        }
        throw new NoSuchElementException("Producto no encontrado con id " + id);
    }
}