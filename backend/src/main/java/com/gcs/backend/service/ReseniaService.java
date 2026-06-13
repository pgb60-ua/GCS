package com.gcs.backend.service;

import com.gcs.backend.dto.ReseniaRequest;
import com.gcs.backend.dto.ReseniaResponse;
import com.gcs.backend.dto.ResumenReseniasResponse;
import com.gcs.backend.model.Coche;
import com.gcs.backend.model.Resenia;
import com.gcs.backend.model.Usuario;
import com.gcs.backend.repository.ReseniaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReseniaService {

    @Autowired
    private ReseniaRepository repository;

    private ReseniaResponse mapToResponse(Resenia resenia) {
        return new ReseniaResponse(
            resenia.getId(),
            resenia.getUsuario().getId(),
            resenia.getCoche().getId(),
            resenia.getUsuario().getNombre(),
            resenia.getCoche().getNomenclatura(),
            resenia.getPuntuacion(),
            resenia.getComentario(),
            resenia.getCreatedAt()
        );
    }

    private Resenia mapFromRequest(ReseniaRequest resenia) {
        Resenia resena = new Resenia();
        Coche coche = new Coche();
        coche.setId(resenia.cocheId());
        resena.setCoche(coche);
        Usuario usuario = new Usuario();
        usuario.setId(resenia.usuarioId());
        resena.setUsuario(usuario);
        resena.setPuntuacion(resenia.puntuacion());
        resena.setComentario(resenia.comentario());
        return resena;
    }

    public List<ReseniaResponse> findAll() {
        // Mapeamos resenia a reseniaResponse
        List<Resenia> resenias = repository.findAll();
        return resenias
            .stream()
            .map(resenia -> mapToResponse(resenia))
            .toList();
    }

    public Optional<ReseniaResponse> findById(UUID id) {
        Optional<ReseniaResponse> resenia = repository
            .findById(id)
            .map(res -> mapToResponse(res));
        return resenia;
    }

    public ReseniaResponse save(ReseniaRequest entity, UUID id) {
        if (entity.puntuacion() < 1 || entity.puntuacion() > 5) {
            throw new IllegalArgumentException(
                "La puntuación debe estar entre 1 y 5"
            );
        }
        if (entity.comentario() == null || entity.comentario().isEmpty()) {
            throw new IllegalArgumentException(
                "El comentario no puede estar vacío"
            );
        }
        if (
            entity.comentario().length() > 500 ||
            entity.comentario().length() < 3
        ) {
            throw new IllegalArgumentException(
                "El comentario debe tener entre 3 y 500 caracteres"
            );
        }
        Resenia resenia = mapFromRequest(entity);
        if (id != null) {
            resenia.setId(id);
        }
        return mapToResponse(repository.save(resenia));
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
