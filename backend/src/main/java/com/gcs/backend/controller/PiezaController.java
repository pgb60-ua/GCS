package com.gcs.backend.controller;

import com.gcs.backend.model.Pieza;
import com.gcs.backend.service.PiezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/piezas")
public class PiezaController {

    @Autowired
    private PiezaService service;

    @GetMapping
    public List<Pieza> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pieza> getById(@PathVariable UUID id) {
        Optional<Pieza> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pieza create(@RequestBody Pieza entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pieza> update(@PathVariable UUID id, @RequestBody Pieza entity) {
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
