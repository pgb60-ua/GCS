package com.gcs.backend.dto;

import java.util.UUID;

public record LoginResponse(
        UUID id,
        String nombre,
        String email,
        String rol
) {
}
