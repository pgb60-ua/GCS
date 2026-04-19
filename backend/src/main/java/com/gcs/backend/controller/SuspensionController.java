package com.gcs.backend.controller;

import com.gcs.backend.model.Suspension;
import com.gcs.backend.service.SuspensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/suspensiones")
public class SuspensionController {

    @Autowired
    private SuspensionService service;

    @GetMapping
    public List<Suspension> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Suspension> getById(@PathVariable UUID id) {
        Optional<Suspension> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Suspension create(@RequestBody Suspension entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Suspension> update(@PathVariable UUID id, @RequestBody Suspension entity) {
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
