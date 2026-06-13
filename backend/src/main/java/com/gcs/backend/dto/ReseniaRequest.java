package com.gcs.backend.dto;

import java.util.UUID;

public record ReseniaRequest(
    UUID usuarioId,
    UUID cocheId,
    Integer puntuacion
) {}
