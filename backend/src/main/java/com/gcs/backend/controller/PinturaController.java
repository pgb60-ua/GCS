package com.gcs.backend.controller;

import com.gcs.backend.model.Pintura;
import com.gcs.backend.service.PinturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pinturas")
public class PinturaController {

    @Autowired
    private PinturaService service;

    @GetMapping
    public List<Pintura> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pintura> getById(@PathVariable UUID id) {
        Optional<Pintura> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pintura create(@RequestBody Pintura entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pintura> update(@PathVariable UUID id, @RequestBody Pintura entity) {
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
