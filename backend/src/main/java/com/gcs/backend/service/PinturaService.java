package com.gcs.backend.service;

import com.gcs.backend.model.Pintura;
import com.gcs.backend.repository.PinturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PinturaService {

    @Autowired
    private PinturaRepository repository;

    public List<Pintura> findAll() {
        return repository.findAll();
    }

    public Optional<Pintura> findById(UUID id) {
        return repository.findById(id);
    }

    public Pintura save(Pintura entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
