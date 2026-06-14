package com.gcs.backend.controller;

import com.gcs.backend.model.Pieza;
import com.gcs.backend.service.PiezaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gcs.backend.dto.PiezaResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/piezas")
public class PiezaController {

    @Autowired
    private PiezaService service;

    @GetMapping
    public List<PiezaResponse> getAll() {
        return service.findAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/catalogo")
    public List<PiezaResponse> getCatalogo() {
        return service.findCatalogo().stream().map(this::toResponse).toList();
    }

    @GetMapping("/tipo/{tipoPieza}")
    public List<PiezaResponse> getByTipo(@PathVariable String tipoPieza) {
        return service.findByTipo(tipoPieza).stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PiezaResponse> getById(@PathVariable UUID id) {
        Optional<Pieza> entity = service.findById(id);
        return entity.map(e -> ResponseEntity.ok(toResponse(e))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PiezaResponse create(@RequestBody com.gcs.backend.dto.PiezaRequest request) {
        Pieza entity = new Pieza();
        entity.setNombre(request.getNombre());
        entity.setTipoPieza(request.getTipoPieza());
        entity.setDescripcion(request.getDescripcion());
        entity.setPrecio(request.getPrecio());
        entity.setDisponible(request.getDisponible());
        entity.setEsCatalogo(request.getEsCatalogo());
        return toResponse(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PiezaResponse> update(@PathVariable UUID id, @RequestBody com.gcs.backend.dto.PiezaRequest request) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Pieza entity = service.findById(id).get();
        entity.setNombre(request.getNombre());
        entity.setTipoPieza(request.getTipoPieza());
        entity.setDescripcion(request.getDescripcion());
        entity.setPrecio(request.getPrecio());
        entity.setDisponible(request.getDisponible());
        entity.setEsCatalogo(request.getEsCatalogo());
        return ResponseEntity.ok(toResponse(service.save(entity)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private PiezaResponse toResponse(Pieza pieza) {
        PiezaResponse response = new PiezaResponse();
        response.setId(pieza.getId());
        response.setNombre(pieza.getNombre());
        response.setTipoPieza(pieza.getTipoPieza());
        response.setDescripcion(pieza.getDescripcion());
        response.setPrecio(pieza.getPrecio());
        response.setDisponible(pieza.getDisponible());
        response.setEsCatalogo(pieza.getEsCatalogo());
        response.setAerodinamica(pieza.getAerodinamica());
        response.setMotor(pieza.getMotor());
        response.setPintura(pieza.getPintura());
        response.setSuspension(pieza.getSuspension());
        return response;
    }
}
