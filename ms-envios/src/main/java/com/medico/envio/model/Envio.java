package com.medico.envio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Order ID is required")
    @Column(name = "pedido_id")
    private Long pedidoId;

    @NotBlank(message = "Delivery address is required")
    @Column(name = "direccion_entrega")
    private String direccionEntrega;

    @NotBlank(message = "City is required")
    @Column(name = "ciudad")
    private String ciudad;

    @NotBlank(message = "Status is required")
    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_envio")
    private LocalDate fechaEnvio;

    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    @Pattern(regexp = "^TRACK[0-9]{9}$", message = "Invalid tracking number format")
    @Column(name = "numero_seguimiento")
    private String numeroSeguimiento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
