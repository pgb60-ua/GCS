package com.gcs.backend.repository;

import com.gcs.backend.model.Coche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CocheRepository extends JpaRepository<Coche, UUID> {
}
