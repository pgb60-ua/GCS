package com.gcs.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadoRequest(
        @NotBlank(message = "El estado de pago es obligatorio")
        String estadoPago
) {
}
