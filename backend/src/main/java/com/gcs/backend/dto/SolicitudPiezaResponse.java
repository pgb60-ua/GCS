package com.gcs.backend.dto;

import java.sql.Timestamp;
import java.util.UUID;

public class SolicitudPiezaResponse {

    private UUID id;
    private UUID usuarioId;
    private String usuarioNombre;
    private UUID cocheId;
    private String cocheNombre;
    private String descripcion;
    private String estado;
    private String respuestaAdmin;
    private Timestamp createdAt;
    private Timestamp revisadoAt;

    public SolicitudPiezaResponse() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public UUID getCocheId() { return cocheId; }
    public void setCocheId(UUID cocheId) { this.cocheId = cocheId; }

    public String getCocheNombre() { return cocheNombre; }
    public void setCocheNombre(String cocheNombre) { this.cocheNombre = cocheNombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getRespuestaAdmin() { return respuestaAdmin; }
    public void setRespuestaAdmin(String respuestaAdmin) { this.respuestaAdmin = respuestaAdmin; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getRevisadoAt() { return revisadoAt; }
    public void setRevisadoAt(Timestamp revisadoAt) { this.revisadoAt = revisadoAt; }
}
