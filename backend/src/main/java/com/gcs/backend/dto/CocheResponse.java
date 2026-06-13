package com.gcs.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CocheResponse {

    private UUID id;
    private String nomenclatura;
    private String equipoF1;
    private String temporada;
    private String descripcion;
    private BigDecimal precioBase;
    private BigDecimal precioTotal;
    private String imagenUrl;
    private Boolean esBase;
    private UUID usuarioId;
    private Timestamp createdAt;
}
