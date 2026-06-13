package com.gcs.backend.dto;

import java.sql.Timestamp;
import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nombre,
        String email,
        String rol,
        Timestamp createdAt
) {
}
