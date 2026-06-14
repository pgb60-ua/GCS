package com.gcs.backend.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public record VentaResponse(
        UUID id,
        UUID usuarioId,
        String usuarioNombre,
        UUID cocheId,
        String cocheNombre,
        BigDecimal montoTotal,
        String estadoPago,
        String metodoPago,
        Timestamp fechaVenta
) {
}
