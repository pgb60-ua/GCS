package com.gcs.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record VentaRequest(
        @NotNull(message = "El usuario es obligatorio")
        UUID usuarioId,

        @NotNull(message = "El coche es obligatorio")
        UUID cocheId,

        @NotBlank(message = "El metodo de pago es obligatorio")
        String metodoPago
) {
}
