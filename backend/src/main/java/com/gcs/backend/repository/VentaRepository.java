package com.gcs.backend.repository;

import com.gcs.backend.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VentaRepository extends JpaRepository<Venta, UUID> {
}
