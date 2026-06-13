package com.gcs.backend.repository;

import com.gcs.backend.model.Resenia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReseniaRepository extends JpaRepository<Resenia, UUID> {
	List<Resenia> findByCocheId(UUID cocheId);

	List<Resenia> findByUsuarioId(UUID usuarioId);

	boolean existsByUsuarioIdAndCocheId(UUID usuarioId, UUID cocheId);
}
