package com.gcs.backend.controller;

import com.gcs.backend.dto.ReseniaRequest;
import com.gcs.backend.dto.ReseniaResponse;
import com.gcs.backend.dto.ResumenReseniasResponse;
import com.gcs.backend.service.ReseniaService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resenias")
public class ReseniaController {

    private final ReseniaService service;

    public ReseniaController(ReseniaService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReseniaResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReseniaResponse> getById(@PathVariable UUID id) {
        Optional<ReseniaResponse> entity = service.findById(id);
        return entity
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/coche/{cocheId}")
    public List<ReseniaResponse> getByCoche(@PathVariable UUID cocheId) {
        return service.findByCocheId(cocheId);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<ReseniaResponse> getByUsuario(@PathVariable UUID usuarioId) {
        return service.findByUsuarioId(usuarioId);
    }

    @GetMapping("/coche/{cocheId}/resumen")
    public ResumenReseniasResponse getResumenByCoche(@PathVariable UUID cocheId) {
        return service.getResumenByCocheId(cocheId);
    }

    @PostMapping
    public ResponseEntity<ReseniaResponse> create(
        @Valid @RequestBody ReseniaRequest entity
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.create(entity));
    }

    @PutMapping("/{id}")
    public ReseniaResponse update(
        @PathVariable UUID id,
        @Valid @RequestBody ReseniaRequest entity
    ) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
