package com.gcs.backend.controller;

import com.gcs.backend.model.Coche;
import com.gcs.backend.service.CocheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/coches")
public class CocheController {

    @Autowired
    private CocheService service;

    @GetMapping
    public List<Coche> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coche> getById(@PathVariable UUID id) {
        Optional<Coche> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Coche create(@RequestBody Coche entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coche> update(@PathVariable UUID id, @RequestBody Coche entity) {
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
