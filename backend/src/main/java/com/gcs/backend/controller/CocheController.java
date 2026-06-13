package com.gcs.backend.controller;

import com.gcs.backend.dto.CocheRequest;
import com.gcs.backend.dto.CocheResponse;
import com.gcs.backend.service.CocheService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/coches")
public class CocheController {

    @Autowired
    private CocheService service;

    @GetMapping
    public List<CocheResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/base")
    public List<CocheResponse> getBase() {
        return service.findBase();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<CocheResponse> getByUsuario(@PathVariable UUID usuarioId) {
        return service.findByUsuario(usuarioId);
    }

    @GetMapping("/equipo/{equipoF1}")
    public List<CocheResponse> getByEquipo(@PathVariable String equipoF1) {
        return service.findByEquipo(equipoF1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CocheResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CocheResponse> create(@Valid @RequestBody CocheRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CocheResponse> update(@PathVariable UUID id, @Valid @RequestBody CocheRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
