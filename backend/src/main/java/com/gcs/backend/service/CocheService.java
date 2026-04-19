package com.gcs.backend.service;

import com.gcs.backend.model.Coche;
import com.gcs.backend.repository.CocheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CocheService {

    @Autowired
    private CocheRepository repository;

    public List<Coche> findAll() {
        return repository.findAll();
    }

    public Optional<Coche> findById(UUID id) {
        return repository.findById(id);
    }

    public Coche save(Coche entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
