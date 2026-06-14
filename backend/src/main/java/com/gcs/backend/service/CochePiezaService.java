package com.gcs.backend.service;

import com.gcs.backend.dto.AsignarPiezaRequest;
import com.gcs.backend.model.Coche;
import com.gcs.backend.model.CochePieza;
import com.gcs.backend.model.Pieza;
import com.gcs.backend.repository.CochePiezaRepository;
import com.gcs.backend.repository.CocheRepository;
import com.gcs.backend.repository.PiezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CochePiezaService {

    @Autowired
    private CochePiezaRepository repository;
    @Autowired
    private CocheRepository cocheRepository;
    @Autowired
    private PiezaRepository piezaRepository;

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CochePieza> findByCocheId(UUID cocheId) {
        if (!cocheRepository.existsById(cocheId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coche no encontrado");
        }
        return repository.findByCocheId(cocheId);
    }

    @Transactional
    public CochePieza assignPieza(UUID cocheId, AsignarPiezaRequest request) {
        Coche coche = cocheRepository.findById(cocheId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coche no encontrado"));
            
        Pieza pieza = piezaRepository.findById(request.getPiezaId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pieza no encontrada"));
            
        if (pieza.getDisponible() != null && !pieza.getDisponible()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La pieza no está disponible");
        }

        List<CochePieza> piezasCoche = repository.findByCocheId(cocheId);
        Optional<CochePieza> existingSameType = piezasCoche.stream()
            .filter(cp -> cp.getPieza().getTipoPieza().equalsIgnoreCase(pieza.getTipoPieza()))
            .findFirst();

        CochePieza cochePieza;
        if (existingSameType.isPresent()) {
            cochePieza = existingSameType.get();
            cochePieza.setPieza(pieza);
            cochePieza.setCantidad(request.getCantidad());
            cochePieza.setNotas(request.getNotas());
        } else {
            cochePieza = new CochePieza();
            cochePieza.setCoche(coche);
            cochePieza.setPieza(pieza);
            cochePieza.setCantidad(request.getCantidad());
            cochePieza.setNotas(request.getNotas());
        }
        
        CochePieza saved = repository.save(cochePieza);
        recalculateCochePrice(cocheId);
        
        return saved;
    }

    @Transactional
    public CochePieza updatePieza(UUID cocheId, UUID cochePiezaId, Integer cantidad, String notas) {
        CochePieza cp = repository.findById(cochePiezaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CochePieza no encontrado"));
            
        if (!cp.getCoche().getId().equals(cocheId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La pieza no pertenece al coche");
        }
        
        cp.setCantidad(cantidad);
        if (notas != null) cp.setNotas(notas);
        
        CochePieza saved = repository.save(cp);
        recalculateCochePrice(cocheId);
        return saved;
    }

    @Transactional
    public void deletePieza(UUID cocheId, UUID cochePiezaId) {
        CochePieza cp = repository.findById(cochePiezaId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CochePieza no encontrado"));
            
        if (!cp.getCoche().getId().equals(cocheId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La pieza no pertenece al coche");
        }
        
        repository.delete(cp);
        repository.flush();
        recalculateCochePrice(cocheId);
    }

    private void recalculateCochePrice(UUID cocheId) {
        Coche coche = cocheRepository.findById(cocheId).orElseThrow();
        List<CochePieza> piezas = repository.findByCocheId(cocheId);
        
        BigDecimal totalPiezas = piezas.stream()
            .map(cp -> cp.getPieza().getPrecio().multiply(new BigDecimal(cp.getCantidad())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        coche.setPrecioTotal(coche.getPrecioBase().add(totalPiezas));
        cocheRepository.save(coche);
    }
}
