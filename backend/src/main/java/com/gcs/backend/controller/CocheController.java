package com.gcs.backend.controller;

import com.gcs.backend.dto.CocheRequest;
import com.gcs.backend.dto.CocheResumenResponse;
import com.gcs.backend.dto.CocheResponse;
import com.gcs.backend.dto.DuplicarCocheRequest;
import com.gcs.backend.service.CocheService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/coches")
public class CocheController {

    private final CocheService service;

    public CocheController(CocheService service) {
        this.service = service;
    }

    @GetMapping
    public List<CocheResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/base")
    public List<CocheResponse> getBaseCars() {
        return service.findByEsBaseTrue();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<CocheResponse> getByUsuario(@PathVariable UUID usuarioId) {
        return service.findByUsuarioId(usuarioId);
    }

    @GetMapping("/equipo/{equipoF1}")
    public List<CocheResponse> getByEquipo(@PathVariable String equipoF1) {
        return service.findByEquipoF1IgnoreCase(equipoF1);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CocheResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}/resumen")
    public CocheResumenResponse getResumen(@PathVariable UUID id) {
        return service.getResumen(id);
    }

    @PostMapping
    public ResponseEntity<CocheResponse> create(@Valid @RequestBody CocheRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PostMapping("/{cocheBaseId}/duplicar")
    public ResponseEntity<CocheResponse> duplicate(@PathVariable UUID cocheBaseId,
                                                   @RequestBody DuplicarCocheRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.duplicate(cocheBaseId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CocheResponse> update(@PathVariable UUID id,
                                                @Valid @RequestBody CocheRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    public CocheResponse patch(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return service.patch(id, updates);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
