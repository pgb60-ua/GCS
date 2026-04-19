package com.gcs.backend.service;

import com.gcs.backend.model.Aerodinamica;
import com.gcs.backend.repository.AerodinamicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AerodinamicaService {

    @Autowired
    private AerodinamicaRepository repository;

    public List<Aerodinamica> findAll() {
        return repository.findAll();
    }

    public Optional<Aerodinamica> findById(UUID id) {
        return repository.findById(id);
    }

    public Aerodinamica save(Aerodinamica entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
