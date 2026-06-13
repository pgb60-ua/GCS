package com.gcs.backend.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record ResumenReseniasResponse(
    UUID cocheId,
    Integer media,
    Integer totalResenias
) {}
