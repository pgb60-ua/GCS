package com.gcs.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class SolicitudPiezaRequest {

    @NotNull(message = "El usuarioId es obligatorio")
    private UUID usuarioId;

    @NotNull(message = "El cocheId es obligatorio")
    private UUID cocheId;

    @NotBlank(message = "La descripcion no puede estar vacia")
    private String descripcion;

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public UUID getCocheId() { return cocheId; }
    public void setCocheId(UUID cocheId) { this.cocheId = cocheId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
