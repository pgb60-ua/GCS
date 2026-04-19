package com.gcs.backend.controller;

import com.gcs.backend.model.SolicitudPieza;
import com.gcs.backend.service.SolicitudPiezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/solicitudes-pieza")
public class SolicitudPiezaController {

    @Autowired
    private SolicitudPiezaService service;

    @GetMapping
    public List<SolicitudPieza> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudPieza> getById(@PathVariable UUID id) {
        Optional<SolicitudPieza> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public SolicitudPieza create(@RequestBody SolicitudPieza entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudPieza> update(@PathVariable UUID id, @RequestBody SolicitudPieza entity) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        entity.setId(id);
        return ResponseEntity.ok(service.save(entity));
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
