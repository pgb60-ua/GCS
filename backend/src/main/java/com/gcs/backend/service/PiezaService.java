package com.gcs.backend.service;

import com.gcs.backend.model.Pieza;
import com.gcs.backend.repository.PiezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PiezaService {

    @Autowired
    private PiezaRepository repository;

    public List<Pieza> findAll() {
        return repository.findAll();
    }

    public Optional<Pieza> findById(UUID id) {
        return repository.findById(id);
    }

    public Pieza save(Pieza entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    public List<Pieza> findCatalogo() {
        return repository.findByEsCatalogoTrue();
    }

    public List<Pieza> findByTipo(String tipo) {
        return repository.findByTipoPiezaIgnoreCase(tipo);
    }
}
