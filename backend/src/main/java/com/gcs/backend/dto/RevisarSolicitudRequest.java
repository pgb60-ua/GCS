package com.gcs.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RevisarSolicitudRequest {

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "APROBADA|RECHAZADA", message = "El estado debe ser APROBADA o RECHAZADA")
    private String estado;

    @NotBlank(message = "La respuesta del admin no puede estar vacia")
    private String respuestaAdmin;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getRespuestaAdmin() { return respuestaAdmin; }
    public void setRespuestaAdmin(String respuestaAdmin) { this.respuestaAdmin = respuestaAdmin; }
}
