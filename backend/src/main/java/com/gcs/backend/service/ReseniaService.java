package com.gcs.backend.service;

import com.gcs.backend.model.Resenia;
import com.gcs.backend.repository.ReseniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReseniaService {

    @Autowired
    private ReseniaRepository repository;

    public List<Resenia> findAll() {
        return repository.findAll();
    }

    public Optional<Resenia> findById(UUID id) {
        return repository.findById(id);
    }

    public Resenia save(Resenia entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
