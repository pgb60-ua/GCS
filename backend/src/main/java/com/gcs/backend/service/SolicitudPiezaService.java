package com.gcs.backend.service;

import com.gcs.backend.model.SolicitudPieza;
import com.gcs.backend.repository.SolicitudPiezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SolicitudPiezaService {

    @Autowired
    private SolicitudPiezaRepository repository;

    public List<SolicitudPieza> findAll() {
        return repository.findAll();
    }

    public Optional<SolicitudPieza> findById(UUID id) {
        return repository.findById(id);
    }

    public SolicitudPieza save(SolicitudPieza entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
