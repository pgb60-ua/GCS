package com.gcs.backend.service;

import com.gcs.backend.model.Motor;
import com.gcs.backend.repository.MotorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MotorService {

    @Autowired
    private MotorRepository repository;

    public List<Motor> findAll() {
        return repository.findAll();
    }

    public Optional<Motor> findById(UUID id) {
        return repository.findById(id);
    }

    public Motor save(Motor entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
