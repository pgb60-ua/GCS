package com.gcs.backend.service;

import com.gcs.backend.dto.RevisarSolicitudRequest;
import com.gcs.backend.dto.SolicitudPiezaRequest;
import com.gcs.backend.dto.SolicitudPiezaResponse;
import com.gcs.backend.exception.BadRequestException;
import com.gcs.backend.exception.ResourceNotFoundException;
import com.gcs.backend.model.Coche;
import com.gcs.backend.model.SolicitudPieza;
import com.gcs.backend.model.Usuario;
import com.gcs.backend.repository.CocheRepository;
import com.gcs.backend.repository.SolicitudPiezaRepository;
import com.gcs.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SolicitudPiezaService {

    private static final Set<String> ESTADOS_VALIDOS = Set.of("PENDIENTE", "APROBADA", "RECHAZADA");

    @Autowired
    private SolicitudPiezaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CocheRepository cocheRepository;

    @Transactional(readOnly = true)
    public List<SolicitudPiezaResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<SolicitudPiezaResponse> findById(UUID id) {
        return repository.findById(id).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<SolicitudPiezaResponse> findByUsuarioId(UUID usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudPiezaResponse> findByCocheId(UUID cocheId) {
        return repository.findByCocheId(cocheId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SolicitudPiezaResponse> findByEstado(String estado) {
        String estadoUpper = estado.toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estadoUpper)) {
            throw new BadRequestException("Estado no valido: " + estado + ". Debe ser PENDIENTE, APROBADA o RECHAZADA.");
        }
        return repository.findByEstadoIgnoreCase(estadoUpper).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public SolicitudPiezaResponse createSolicitud(SolicitudPiezaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + request.getUsuarioId()));

        Coche coche = cocheRepository.findById(request.getCocheId())
                .orElseThrow(() -> new ResourceNotFoundException("Coche no encontrado con id: " + request.getCocheId()));

        SolicitudPieza solicitud = new SolicitudPieza();
        solicitud.setUsuario(usuario);
        solicitud.setCoche(coche);
        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setEstado("PENDIENTE");

        return toResponse(repository.save(solicitud));
    }

    @Transactional
    public SolicitudPiezaResponse revisarSolicitud(UUID id, RevisarSolicitudRequest request) {
        SolicitudPieza solicitud = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + id));

        String estadoUpper = request.getEstado().toUpperCase();
        if (!Set.of("APROBADA", "RECHAZADA").contains(estadoUpper)) {
            throw new BadRequestException("El estado de revision debe ser APROBADA o RECHAZADA.");
        }

        solicitud.setEstado(estadoUpper);
        solicitud.setRespuestaAdmin(request.getRespuestaAdmin());
        solicitud.setRevisadoAt(Timestamp.from(Instant.now()));

        return toResponse(repository.save(solicitud));
    }

    @Transactional
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Solicitud no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }

    private SolicitudPiezaResponse toResponse(SolicitudPieza s) {
        SolicitudPiezaResponse dto = new SolicitudPiezaResponse();
        dto.setId(s.getId());
        dto.setDescripcion(s.getDescripcion());
        dto.setEstado(s.getEstado());
        dto.setRespuestaAdmin(s.getRespuestaAdmin());
        dto.setCreatedAt(s.getCreatedAt());
        dto.setRevisadoAt(s.getRevisadoAt());

        if (s.getUsuario() != null) {
            dto.setUsuarioId(s.getUsuario().getId());
            dto.setUsuarioNombre(s.getUsuario().getNombre());
        }
        if (s.getCoche() != null) {
            dto.setCocheId(s.getCoche().getId());
            dto.setCocheNombre(s.getCoche().getNomenclatura());
        }
        return dto;
    }
}

