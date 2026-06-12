package com.gcs.backend.service;

import com.gcs.backend.dto.LoginRequest;
import com.gcs.backend.dto.LoginResponse;
import com.gcs.backend.dto.UsuarioRequest;
import com.gcs.backend.dto.UsuarioResponse;
import com.gcs.backend.exception.BadRequestException;
import com.gcs.backend.exception.ResourceNotFoundException;
import com.gcs.backend.model.Usuario;
import com.gcs.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<UsuarioResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponse findById(UUID id) {
        return toResponse(findEntityById(id));
    }

    public UsuarioResponse create(UsuarioRequest request) {
        String email = normalizeEmail(request.email());
        if (repository.existsByEmail(email)) {
            throw new BadRequestException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        applyRequest(usuario, request);
        usuario.setEmail(email);
        return toResponse(repository.save(usuario));
    }

    public UsuarioResponse update(UUID id, UsuarioRequest request) {
        Usuario usuario = findEntityById(id);
        String email = normalizeEmail(request.email());
        repository.findByEmail(email)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BadRequestException("Ya existe un usuario con ese email");
                });

        applyRequest(usuario, request);
        usuario.setEmail(email);
        return toResponse(repository.save(usuario));
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = repository.findByEmail(normalizeEmail(request.email()))
                .orElseThrow(() -> new BadRequestException("Email o password incorrectos"));

        if (!usuario.getPassword().equals(request.password())) {
            throw new BadRequestException("Email o password incorrectos");
        }

        return new LoginResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        repository.deleteById(id);
    }

    private Usuario findEntityById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private void applyRequest(Usuario usuario, UsuarioRequest request) {
        usuario.setNombre(request.nombre().trim());
        usuario.setPassword(request.password());
        usuario.setRol(normalizeRole(request.rol()));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeRole(String role) {
        String normalized = role.trim().toUpperCase(Locale.ROOT);
        if (!normalized.equals("CLIENTE") && !normalized.equals("ADMIN")) {
            throw new BadRequestException("El rol debe ser CLIENTE o ADMIN");
        }
        return normalized;
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getCreatedAt()
        );
    }
}
