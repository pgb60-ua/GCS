package com.gcs.backend.service;

import com.gcs.backend.model.CochePieza;
import com.gcs.backend.repository.CochePiezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CochePiezaService {

    @Autowired
    private CochePiezaRepository repository;

    public List<CochePieza> findAll() {
        return repository.findAll();
    }

    public Optional<CochePieza> findById(UUID id) {
        return repository.findById(id);
    }

    public CochePieza save(CochePieza entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
