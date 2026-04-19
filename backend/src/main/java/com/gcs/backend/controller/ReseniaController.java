package com.gcs.backend.controller;

import com.gcs.backend.model.Resenia;
import com.gcs.backend.service.ReseniaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/resenias")
public class ReseniaController {

    @Autowired
    private ReseniaService service;

    @GetMapping
    public List<Resenia> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resenia> getById(@PathVariable UUID id) {
        Optional<Resenia> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Resenia create(@RequestBody Resenia entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resenia> update(@PathVariable UUID id, @RequestBody Resenia entity) {
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
