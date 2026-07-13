package com.duoc.ms_pacientes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad Paciente del sistema médico")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del paciente", example = "1")
    private Integer id;

    @NotBlank(message = "El RUN no puede estar vacío")
    @Column(nullable = false, unique = true)
    @Schema(description = "RUN del paciente sin dígito verificador", example = "12345678", required = true)
    private String run;

    @NotBlank(message = "Los nombres no pueden estar vacíos")
    @Column(nullable = false)
    @Schema(description = "Nombres del paciente", example = "Juan", required = true)
    private String nombres;

    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    @Column(nullable = false)
    @Schema(description = "Apellidos del paciente", example = "Pérez García", required = true)
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    @Column(nullable = false)
    @Schema(description = "Fecha de nacimiento del paciente", example = "1990-05-15", required = true)
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo debe ser válido")
    @Column(nullable = false, unique = true)
    @Schema(description = "Email del paciente", example = "juan.perez@example.com", required = true)
    private String correo;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Fecha de creación del registro")
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }
}
