package com.gcs.backend.dto;

import java.util.UUID;

public record ResumenReseniasResponse(
    UUID cocheId,
    Integer media,
    Integer totalResenias
) {}
