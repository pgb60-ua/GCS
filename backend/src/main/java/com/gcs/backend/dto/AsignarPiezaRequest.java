package com.gcs.backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

@Data
public class AsignarPiezaRequest {
    private UUID cocheId;

    @NotNull(message = "La piezaId es obligatoria")
    private UUID piezaId;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;
    
    private String notas;
}
