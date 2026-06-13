package com.gcs.backend.repository;

import com.gcs.backend.model.Coche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CocheRepository extends JpaRepository<Coche, UUID> {

    List<Coche> findByEsBaseTrue();

    List<Coche> findByUsuarioId(UUID usuarioId);

    List<Coche> findByEquipoF1IgnoreCase(String equipoF1);
}
