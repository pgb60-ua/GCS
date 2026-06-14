package com.gcs.backend.repository;

import com.gcs.backend.model.CochePieza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CochePiezaRepository extends JpaRepository<CochePieza, UUID> {
    @org.springframework.data.jpa.repository.Query("SELECT cp FROM CochePieza cp JOIN FETCH cp.pieza WHERE cp.coche.id = :cocheId")
    List<CochePieza> findByCocheId(@org.springframework.data.repository.query.Param("cocheId") UUID cocheId);
}
