package com.gcs.backend.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

public record CocheResponse(
        UUID id,
        String nomenclatura,
        String equipoF1,
        String temporada,
        String descripcion,
        BigDecimal precioBase,
        BigDecimal precioTotal,
        String imagenUrl,
        Boolean esBase,
        UUID usuarioId,
        Timestamp createdAt
) {
}
