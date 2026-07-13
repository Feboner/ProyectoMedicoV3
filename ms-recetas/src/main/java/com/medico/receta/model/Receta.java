package com.medico.receta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "receta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El paciente es obligatorio")
    @Column(name = "paciente_id")
    private Long pacienteId;

    @Column(name = "medicamento")
    private String medicamento;

    @Column(name = "indicaciones")
    private String indicaciones;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
