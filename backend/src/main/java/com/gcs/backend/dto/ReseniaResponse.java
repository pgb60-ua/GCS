package com.gcs.backend.dto;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ReseniaResponse(
    UUID id,
    UUID usuarioId,
    UUID cocheId,
    String usuarioNombre,
    String cocheNombre,
    Integer puntuacion,
    String comentario,
    Timestamp createdAt
) {}
