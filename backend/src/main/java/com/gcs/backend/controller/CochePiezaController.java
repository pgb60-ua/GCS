package com.gcs.backend.controller;

import com.gcs.backend.dto.AsignarPiezaRequest;
import com.gcs.backend.dto.CochePiezaResponse;
import com.gcs.backend.model.CochePieza;
import com.gcs.backend.service.CochePiezaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/coches")
public class CochePiezaController {

    @Autowired
    private CochePiezaService service;

    @GetMapping("/{cocheId}/piezas")
    public List<CochePiezaResponse> getByCocheId(@PathVariable UUID cocheId) {
        return service.findByCocheId(cocheId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @PostMapping("/{cocheId}/piezas")
    public CochePiezaResponse assignPieza(@PathVariable UUID cocheId, @Valid @RequestBody AsignarPiezaRequest request) {
        return toResponse(service.assignPieza(cocheId, request));
    }

    @PutMapping("/{cocheId}/piezas/{cochePiezaId}")
    public CochePiezaResponse updatePieza(
            @PathVariable UUID cocheId, 
            @PathVariable UUID cochePiezaId, 
            @RequestBody AsignarPiezaRequest request) {
        return toResponse(service.updatePieza(cocheId, cochePiezaId, request.getCantidad(), request.getNotas()));
    }

    @DeleteMapping("/{cocheId}/piezas/{cochePiezaId}")
    public ResponseEntity<Void> deletePieza(@PathVariable UUID cocheId, @PathVariable UUID cochePiezaId) {
        service.deletePieza(cocheId, cochePiezaId);
        return ResponseEntity.noContent().build();
    }

    private CochePiezaResponse toResponse(CochePieza cp) {
        CochePiezaResponse response = new CochePiezaResponse();
        response.setId(cp.getId());
        response.setCocheId(cp.getCoche().getId());
        response.setPiezaId(cp.getPieza().getId());
        response.setNombrePieza(cp.getPieza().getNombre());
        response.setTipoPieza(cp.getPieza().getTipoPieza());
        response.setPrecio(cp.getPieza().getPrecio());
        response.setCantidad(cp.getCantidad());
        response.setSubtotal(cp.getPieza().getPrecio().multiply(new BigDecimal(cp.getCantidad())));
        response.setNotas(cp.getNotas());
        return response;
    }
}
