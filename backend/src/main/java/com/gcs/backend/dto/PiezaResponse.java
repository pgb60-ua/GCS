package com.gcs.backend.dto;

import com.gcs.backend.model.Aerodinamica;
import com.gcs.backend.model.Motor;
import com.gcs.backend.model.Pintura;
import com.gcs.backend.model.Suspension;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PiezaResponse {
    private UUID id;
    private String nombre;
    private String tipoPieza;
    private String descripcion;
    private BigDecimal precio;
    private Boolean disponible;
    private Boolean esCatalogo;
    
    private Aerodinamica aerodinamica;
    private Motor motor;
    private Pintura pintura;
    private Suspension suspension;
}
