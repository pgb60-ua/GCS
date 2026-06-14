package com.gcs.backend.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CochePiezaResponse {
    private UUID id;
    private UUID cocheId;
    private UUID piezaId;
    private String nombrePieza;
    private String tipoPieza;
    private BigDecimal precio;
    private Integer cantidad;
    private BigDecimal subtotal;
    private String notas;
}
