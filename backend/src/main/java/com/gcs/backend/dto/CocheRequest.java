package com.gcs.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CocheRequest {

    @NotBlank(message = "La nomenclatura es obligatoria")
    private String nomenclatura;

    @NotBlank(message = "El equipo es obligatorio")
    private String equipoF1;

    @NotBlank(message = "La temporada es obligatoria")
    private String temporada;

    private String descripcion;

    @DecimalMin(value = "0.0", message = "El precio base debe ser positivo o cero")
    private BigDecimal precioBase;

    private String imagenUrl;

    private Boolean esBase;

    private UUID usuarioId;
}
