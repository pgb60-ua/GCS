package com.gcs.backend.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record ReseniaRequest(
    UUID usuarioId,
    UUID cocheId,
    Integer puntuacion,
    String comentario
) {}
