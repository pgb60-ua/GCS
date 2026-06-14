package com.gcs.backend.service;

import com.gcs.backend.dto.CocheRequest;
import com.gcs.backend.dto.CocheResumenResponse;
import com.gcs.backend.dto.CocheResponse;
import com.gcs.backend.dto.DuplicarCocheRequest;
import com.gcs.backend.exception.BadRequestException;
import com.gcs.backend.exception.ResourceNotFoundException;
import com.gcs.backend.model.Coche;
import com.gcs.backend.model.CochePieza;
import com.gcs.backend.model.Usuario;
import com.gcs.backend.repository.CochePiezaRepository;
import com.gcs.backend.repository.CocheRepository;
import com.gcs.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CocheService {

    private final CocheRepository cocheRepository;
    private final UsuarioRepository usuarioRepository;
    private final CochePiezaRepository cochePiezaRepository;

    public CocheService(CocheRepository cocheRepository,
                        UsuarioRepository usuarioRepository,
                        CochePiezaRepository cochePiezaRepository) {
        this.cocheRepository = cocheRepository;
        this.usuarioRepository = usuarioRepository;
        this.cochePiezaRepository = cochePiezaRepository;
    }

    public List<CocheResponse> findAll() {
        return cocheRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CocheResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public List<CocheResponse> findByEsBaseTrue() {
        return cocheRepository.findByEsBaseTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CocheResponse> findByUsuarioId(UUID usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        return cocheRepository.findByUsuarioId(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CocheResponse> findByEquipoF1IgnoreCase(String equipoF1) {
        return cocheRepository.findByEquipoF1IgnoreCase(equipoF1).stream()
                .map(this::toResponse)
                .toList();
    }

    public CocheResponse create(CocheRequest request) {
        Coche coche = new Coche();
        applyRequest(coche, request);
        if (Boolean.TRUE.equals(coche.getEsBase())) {
            coche.setPrecioTotal(coche.getPrecioBase());
        }
        return toResponse(cocheRepository.save(coche));
    }

    public CocheResponse update(UUID id, CocheRequest request) {
        Coche coche = findEntityById(id);
        applyRequest(coche, request);
        if (Boolean.TRUE.equals(coche.getEsBase())) {
            coche.setPrecioTotal(coche.getPrecioBase());
        }
        return toResponse(cocheRepository.save(coche));
    }

    public CocheResponse patch(UUID id, Map<String, Object> updates) {
        Coche coche = findEntityById(id);
        if (Boolean.TRUE.equals(coche.getEsBase())) {
            throw new BadRequestException("No se puede modificar un coche base desde personalizacion");
        }

        if (updates.containsKey("nomenclatura")) {
            coche.setNomenclatura((String) updates.get("nomenclatura"));
        }
        if (updates.containsKey("descripcion")) {
            coche.setDescripcion((String) updates.get("descripcion"));
        }
        if (updates.containsKey("imagenUrl")) {
            coche.setImagenUrl((String) updates.get("imagenUrl"));
        }

        return toResponse(cocheRepository.save(coche));
    }

    public CocheResponse duplicate(UUID cocheBaseId, DuplicarCocheRequest request) {
        Coche base = findEntityById(cocheBaseId);
        if (!Boolean.TRUE.equals(base.getEsBase())) {
            throw new BadRequestException("El coche origen no es un coche base");
        }

        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Coche nuevo = new Coche();
        nuevo.setNomenclatura(base.getNomenclatura());
        nuevo.setEquipoF1(base.getEquipoF1());
        nuevo.setTemporada(base.getTemporada());
        nuevo.setDescripcion(base.getDescripcion());
        nuevo.setPrecioBase(base.getPrecioBase());
        nuevo.setPrecioTotal(base.getPrecioBase());
        nuevo.setImagenUrl(base.getImagenUrl());
        nuevo.setEsBase(false);
        nuevo.setUsuario(usuario);

        return toResponse(cocheRepository.save(nuevo));
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public CocheResumenResponse getResumen(UUID id) {
        Coche coche = findEntityById(id);
        List<CochePieza> cochePiezas = cochePiezaRepository.findByCocheId(id);

        BigDecimal precioBase = coche.getPrecioBase() != null ? coche.getPrecioBase() : BigDecimal.ZERO;
        BigDecimal sumaPiezas = BigDecimal.ZERO;

        List<CocheResumenResponse.PiezaResumenDTO> piezas = cochePiezas.stream().map(cp -> {
            BigDecimal precio = cp.getPieza().getPrecio() != null ? cp.getPieza().getPrecio() : BigDecimal.ZERO;
            int cantidad = cp.getCantidad() != null ? cp.getCantidad() : 0;
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
            return new CocheResumenResponse.PiezaResumenDTO(
                    cp.getPieza().getId(),
                    cp.getPieza().getNombre(),
                    cp.getPieza().getTipoPieza(),
                    precio,
                    cantidad,
                    subtotal,
                    cp.getNotas()
            );
        }).collect(Collectors.toList());

        for (CocheResumenResponse.PiezaResumenDTO p : piezas) {
            sumaPiezas = sumaPiezas.add(p.subtotal());
        }

        BigDecimal precioTotal = precioBase.add(sumaPiezas);

        return new CocheResumenResponse(
                coche.getId(),
                coche.getNomenclatura(),
                coche.getEquipoF1(),
                coche.getTemporada(),
                coche.getDescripcion(),
                precioBase,
                precioTotal,
                coche.getImagenUrl(),
                coche.getEsBase(),
                coche.getUsuario() != null ? coche.getUsuario().getId() : null,
                piezas
        );
    }

    public void deleteById(UUID id) {
        Coche coche = cocheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coche no encontrado"));
        cocheRepository.delete(coche);
    }

    public Coche findEntityById(UUID id) {
        return cocheRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coche no encontrado"));
    }

    private void applyRequest(Coche coche, CocheRequest request) {
        coche.setNomenclatura(request.nomenclatura());
        coche.setEquipoF1(request.equipoF1());
        coche.setTemporada(request.temporada());
        coche.setDescripcion(request.descripcion());
        coche.setPrecioBase(request.precioBase());
        coche.setImagenUrl(request.imagenUrl());
        if (request.esBase() != null) {
            coche.setEsBase(request.esBase());
        }
        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(request.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            coche.setUsuario(usuario);
        }
    }

    public CocheResponse toResponse(Coche coche) {
        return new CocheResponse(
                coche.getId(),
                coche.getNomenclatura(),
                coche.getEquipoF1(),
                coche.getTemporada(),
                coche.getDescripcion(),
                coche.getPrecioBase(),
                coche.getPrecioTotal(),
                coche.getImagenUrl(),
                coche.getEsBase(),
                coche.getUsuario() != null ? coche.getUsuario().getId() : null,
                coche.getCreatedAt()
        );
    }
}
