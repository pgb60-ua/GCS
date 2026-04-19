package com.gcs.backend.repository;

import com.gcs.backend.model.Pieza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PiezaRepository extends JpaRepository<Pieza, UUID> {
}
