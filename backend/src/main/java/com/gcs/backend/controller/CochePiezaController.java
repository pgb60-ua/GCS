package com.gcs.backend.controller;

import com.gcs.backend.model.CochePieza;
import com.gcs.backend.service.CochePiezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/coche-piezas")
public class CochePiezaController {

    @Autowired
    private CochePiezaService service;

    @GetMapping
    public List<CochePieza> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CochePieza> getById(@PathVariable UUID id) {
        Optional<CochePieza> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CochePieza create(@RequestBody CochePieza entity) {
        return service.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CochePieza> update(@PathVariable UUID id, @RequestBody CochePieza entity) {
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
