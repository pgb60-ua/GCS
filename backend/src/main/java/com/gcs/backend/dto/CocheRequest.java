package com.gcs.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record CocheRequest(
        @NotBlank(message = "La nomenclatura es obligatoria")
        String nomenclatura,

        @NotBlank(message = "El equipo de F1 es obligatorio")
        String equipoF1,

        @NotBlank(message = "La temporada es obligatoria")
        String temporada,

        String descripcion,

        @PositiveOrZero(message = "El precio base debe ser positivo o cero")
        BigDecimal precioBase,

        String imagenUrl,

        Boolean esBase,

        UUID usuarioId
) {
}
