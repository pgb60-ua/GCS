package com.gcs.backend.service;

import com.gcs.backend.dto.CocheRequest;
import com.gcs.backend.dto.CocheResponse;
import com.gcs.backend.exception.ResourceNotFoundException;
import com.gcs.backend.model.Coche;
import com.gcs.backend.model.Usuario;
import com.gcs.backend.repository.CocheRepository;
import com.gcs.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CocheService {

    @Autowired
    private CocheRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<CocheResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public List<CocheResponse> findBase() {
        return repository.findByEsBaseTrue().stream().map(this::toResponse).toList();
    }

    public List<CocheResponse> findByUsuario(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream().map(this::toResponse).toList();
    }

    public List<CocheResponse> findByEquipo(String equipoF1) {
        return repository.findByEquipoF1IgnoreCase(equipoF1).stream().map(this::toResponse).toList();
    }

    public CocheResponse findById(UUID id) {
        Coche coche = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coche no encontrado con id: " + id));
        return toResponse(coche);
    }

    public CocheResponse create(CocheRequest request) {
        Coche coche = new Coche();
        applyRequest(request, coche);
        if (Boolean.TRUE.equals(coche.getEsBase())) {
            coche.setPrecioTotal(coche.getPrecioBase());
        }
        return toResponse(repository.save(coche));
    }

    public CocheResponse update(UUID id, CocheRequest request) {
        Coche coche = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coche no encontrado con id: " + id));
        applyRequest(request, coche);
        if (Boolean.TRUE.equals(coche.getEsBase())) {
            coche.setPrecioTotal(coche.getPrecioBase());
        }
        return toResponse(repository.save(coche));
    }

    public void deleteById(UUID id) {
        Coche coche = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coche no encontrado con id: " + id));
        repository.delete(coche);
    }

    public Coche findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coche no encontrado con id: " + id));
    }

    private void applyRequest(CocheRequest request, Coche coche) {
        coche.setNomenclatura(request.getNomenclatura());
        coche.setEquipoF1(request.getEquipoF1());
        coche.setTemporada(request.getTemporada());
        coche.setDescripcion(request.getDescripcion());
        coche.setPrecioBase(request.getPrecioBase());
        coche.setImagenUrl(request.getImagenUrl());
        // Preserve existing esBase if not explicitly sent in the request
        if (request.getEsBase() != null) {
            coche.setEsBase(request.getEsBase());
        }
        // Preserve existing usuario if usuarioId not explicitly sent in the request
        if (request.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + request.getUsuarioId()));
            coche.setUsuario(usuario);
        }
    }

    public CocheResponse toResponse(Coche coche) {
        CocheResponse r = new CocheResponse();
        r.setId(coche.getId());
        r.setNomenclatura(coche.getNomenclatura());
        r.setEquipoF1(coche.getEquipoF1());
        r.setTemporada(coche.getTemporada());
        r.setDescripcion(coche.getDescripcion());
        r.setPrecioBase(coche.getPrecioBase());
        r.setPrecioTotal(coche.getPrecioTotal());
        r.setImagenUrl(coche.getImagenUrl());
        r.setEsBase(coche.getEsBase());
        r.setCreatedAt(coche.getCreatedAt());
        if (coche.getUsuario() != null) {
            r.setUsuarioId(coche.getUsuario().getId());
        }
        return r;
    }
}
