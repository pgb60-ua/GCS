package com.gcs.backend.service;

import com.gcs.backend.model.Suspension;
import com.gcs.backend.repository.SuspensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SuspensionService {

    @Autowired
    private SuspensionRepository repository;

    public List<Suspension> findAll() {
        return repository.findAll();
    }

    public Optional<Suspension> findById(UUID id) {
        return repository.findById(id);
    }

    public Suspension save(Suspension entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
