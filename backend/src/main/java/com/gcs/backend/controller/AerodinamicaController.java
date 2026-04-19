package com.gcs.backend.controller;

import com.gcs.backend.model.Aerodinamica;
import com.gcs.backend.service.AerodinamicaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/aerodinamica")
public class AerodinamicaController {

    @Autowired
    private AerodinamicaService service;

    @GetMapping
    public List<Aerodinamica> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aerodinamica> getById(@PathVariable UUID id) {
        Optional<Aerodinamica> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Aerodinamica create(@RequestBody Aerodinamica entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aerodinamica> update(@PathVariable UUID id, @RequestBody Aerodinamica entity) {
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
