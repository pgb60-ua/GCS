package com.gcs.backend.controller;

import com.gcs.backend.dto.RevisarSolicitudRequest;
import com.gcs.backend.dto.SolicitudPiezaRequest;
import com.gcs.backend.dto.SolicitudPiezaResponse;
import com.gcs.backend.service.SolicitudPiezaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/solicitudes-pieza")
public class SolicitudPiezaController {

    @Autowired
    private SolicitudPiezaService service;

    // GET /api/solicitudes-pieza
    public List<SolicitudPiezaResponse> getAll() {
        return service.findAll();
    }
    // GET /api/solicitudes-pieza/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudPiezaResponse> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<SolicitudPiezaResponse> getByUsuario(@PathVariable UUID usuarioId) {
        return service.findByUsuarioId(usuarioId);
    }

    @GetMapping("/coche/{cocheId}")
    public List<SolicitudPiezaResponse> getByCoche(@PathVariable UUID cocheId) {
        return service.findByCocheId(cocheId);
    }

    @GetMapping("/estado/{estado}")
    public List<SolicitudPiezaResponse> getByEstado(@PathVariable String estado) {
        return service.findByEstado(estado);
    }

    @PostMapping
    public ResponseEntity<SolicitudPiezaResponse> create(@Valid @RequestBody SolicitudPiezaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSolicitud(request));
    }

    @PatchMapping("/{id}/revisar")
    public ResponseEntity<SolicitudPiezaResponse> revisar(
            @PathVariable UUID id,
            @Valid @RequestBody RevisarSolicitudRequest request) {
        return ResponseEntity.ok(service.revisarSolicitud(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
