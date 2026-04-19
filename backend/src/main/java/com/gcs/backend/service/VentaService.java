package com.gcs.backend.service;

import com.gcs.backend.model.Venta;
import com.gcs.backend.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VentaService {

    @Autowired
    private VentaRepository repository;

    public List<Venta> findAll() {
        return repository.findAll();
    }

    public Optional<Venta> findById(UUID id) {
        return repository.findById(id);
    }

    public Venta save(Venta entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
