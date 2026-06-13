package com.gcs.backend.controller;

import com.gcs.backend.dto.ReseniaRequest;
import com.gcs.backend.dto.ReseniaResponse;
import com.gcs.backend.service.ReseniaService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resenias")
public class ReseniaController {

    @Autowired
    private ReseniaService service;

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

    @PostMapping
    public ReseniaResponse create(@RequestBody ReseniaRequest entity) {
        return service.save(entity, null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReseniaResponse> update(
        @PathVariable UUID id,
        @RequestBody ReseniaRequest entity
    ) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
