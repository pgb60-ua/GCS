package com.gcs.backend.service;

import com.gcs.backend.dto.VentaRequest;
import com.gcs.backend.dto.VentaResponse;
import com.gcs.backend.exception.BadRequestException;
import com.gcs.backend.exception.ResourceNotFoundException;
import com.gcs.backend.model.Coche;
import com.gcs.backend.model.EstadoPago;
import com.gcs.backend.model.Usuario;
import com.gcs.backend.model.Venta;
import com.gcs.backend.repository.CocheRepository;
import com.gcs.backend.repository.UsuarioRepository;
import com.gcs.backend.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CocheRepository cocheRepository;

    public VentaService(VentaRepository ventaRepository,
                        UsuarioRepository usuarioRepository,
                        CocheRepository cocheRepository) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cocheRepository = cocheRepository;
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> findAll() {
        return ventaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VentaResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> findByUsuarioId(UUID usuarioId) {
        return ventaRepository.findByUsuarioIdOrderByFechaVentaDesc(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public VentaResponse create(VentaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Coche coche = cocheRepository.findById(request.cocheId())
                .orElseThrow(() -> new ResourceNotFoundException("Coche no encontrado"));

        if (Boolean.TRUE.equals(coche.getEsBase())) {
            throw new BadRequestException("No se puede comprar un coche base");
        }
        if (ventaRepository.existsByCocheId(coche.getId())) {
            throw new BadRequestException("Este coche ya tiene una venta registrada");
        }

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setCoche(coche);
        venta.setMetodoPago(request.metodoPago().trim());
        venta.setEstadoPago(EstadoPago.PAGADO);
        venta.setMontoTotal(resolveMontoTotal(coche));
        venta.setFechaVenta(Timestamp.from(Instant.now()));

        return toResponse(ventaRepository.save(venta));
    }

    @Transactional
    public VentaResponse updateEstado(UUID id, String estadoPago) {
        Venta venta = findEntityById(id);
        venta.setEstadoPago(parseEstadoPago(estadoPago));
        return toResponse(ventaRepository.save(venta));
    }

    @Transactional
    public void deleteById(UUID id) {
        Venta venta = findEntityById(id);
        ventaRepository.delete(venta);
    }

    private Venta findEntityById(UUID id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));
    }

    private EstadoPago parseEstadoPago(String estadoPago) {
        if (estadoPago == null || estadoPago.trim().isEmpty()) {
            throw new BadRequestException("El estado de pago es obligatorio");
        }
        try {
            return EstadoPago.valueOf(estadoPago.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("El estado de pago debe ser PENDIENTE, PAGADO o CANCELADO");
        }
    }

    private BigDecimal resolveMontoTotal(Coche coche) {
        if (coche.getPrecioTotal() != null) {
            return coche.getPrecioTotal();
        }
        if (coche.getPrecioBase() != null) {
            return coche.getPrecioBase();
        }
        throw new BadRequestException("El coche no tiene precio configurado");
    }

    private VentaResponse toResponse(Venta venta) {
        Usuario usuario = venta.getUsuario();
        Coche coche = venta.getCoche();
        return new VentaResponse(
                venta.getId(),
                usuario != null ? usuario.getId() : null,
                usuario != null ? usuario.getNombre() : null,
                coche != null ? coche.getId() : null,
                coche != null ? coche.getNomenclatura() : null,
                venta.getMontoTotal(),
                venta.getEstadoPago() != null ? venta.getEstadoPago().name() : null,
                venta.getMetodoPago(),
                venta.getFechaVenta()
        );
    }
}
