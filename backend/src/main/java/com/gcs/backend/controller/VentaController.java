package com.gcs.backend.controller;

import com.gcs.backend.dto.EstadoRequest;
import com.gcs.backend.dto.VentaRequest;
import com.gcs.backend.dto.VentaResponse;
import com.gcs.backend.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService service;

    public VentaController(VentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<VentaResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public VentaResponse getById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<VentaResponse> getByUsuario(@PathVariable UUID usuarioId) {
        return service.findByUsuarioId(usuarioId);
    }

    @PostMapping
    public ResponseEntity<VentaResponse> create(@Valid @RequestBody VentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PatchMapping("/{id}/estado")
    public VentaResponse updateEstado(@PathVariable UUID id, @Valid @RequestBody EstadoRequest request) {
        return service.updateEstado(id, request.estadoPago());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
