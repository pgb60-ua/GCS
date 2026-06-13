package com.gcs.backend.controller;

import com.gcs.backend.dto.UsuarioRequest;
import com.gcs.backend.dto.UsuarioPerfilUpdateRequest;
import com.gcs.backend.dto.UsuarioResponse;
import com.gcs.backend.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UsuarioResponse getById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public UsuarioResponse update(
        @PathVariable UUID id,
        @Valid @RequestBody UsuarioPerfilUpdateRequest request
    ) {
        return service.updateProfile(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
