package com.gcs.backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CocheResumenResponse(
        UUID id,
        String nomenclatura,
        String equipoF1,
        String temporada,
        String descripcion,
        BigDecimal precioBase,
        BigDecimal precioTotal,
        String imagenUrl,
        Boolean esBase,
        UUID usuarioId,
        List<PiezaResumenDTO> piezas
) {

    public record PiezaResumenDTO(
            UUID id,
            String nombre,
            String tipoPieza,
            BigDecimal precio,
            Integer cantidad,
            BigDecimal subtotal,
            String notas
    ) {
    }
}
