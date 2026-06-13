package com.gcs.backend.dto;

import java.util.UUID;
import jdk.jfr.Timestamp;

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
