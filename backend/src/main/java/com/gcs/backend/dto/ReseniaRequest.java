package com.gcs.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReseniaRequest(
    @NotNull(message = "El usuario es obligatorio")
    UUID usuarioId,
    @NotNull(message = "El coche es obligatorio")
    UUID cocheId,
    @NotNull(message = "La puntuacion es obligatoria")
    @Min(value = 1, message = "La puntuacion debe estar entre 1 y 5")
    @Max(value = 5, message = "La puntuacion debe estar entre 1 y 5")
    Integer puntuacion,
    @NotBlank(message = "El comentario es obligatorio")
    @Size(min = 3, max = 500, message = "El comentario debe tener entre 3 y 500 caracteres")
    String comentario
) {}
