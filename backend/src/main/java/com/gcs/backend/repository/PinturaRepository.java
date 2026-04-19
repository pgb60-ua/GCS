package com.gcs.backend.repository;

import com.gcs.backend.model.Pintura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PinturaRepository extends JpaRepository<Pintura, UUID> {
}
