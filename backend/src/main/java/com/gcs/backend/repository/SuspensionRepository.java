package com.gcs.backend.repository;

import com.gcs.backend.model.Suspension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SuspensionRepository extends JpaRepository<Suspension, UUID> {
}
