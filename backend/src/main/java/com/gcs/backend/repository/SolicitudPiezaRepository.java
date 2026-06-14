package com.gcs.backend.repository;

import com.gcs.backend.model.SolicitudPieza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SolicitudPiezaRepository extends JpaRepository<SolicitudPieza, UUID> {

    List<SolicitudPieza> findByUsuarioId(UUID usuarioId);

    List<SolicitudPieza> findByCocheId(UUID cocheId);

    List<SolicitudPieza> findByEstadoIgnoreCase(String estado);
}
