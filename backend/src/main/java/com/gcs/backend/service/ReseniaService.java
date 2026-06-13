package com.gcs.backend.service;

import com.gcs.backend.dto.ReseniaRequest;
import com.gcs.backend.dto.ReseniaResponse;
import com.gcs.backend.dto.ResumenReseniasResponse;
import com.gcs.backend.exception.BadRequestException;
import com.gcs.backend.exception.ResourceNotFoundException;
import com.gcs.backend.model.Coche;
import com.gcs.backend.model.Resenia;
import com.gcs.backend.model.Usuario;
import com.gcs.backend.repository.CocheRepository;
import com.gcs.backend.repository.ReseniaRepository;
import com.gcs.backend.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReseniaService {

    private final ReseniaRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final CocheRepository cocheRepository;

    public ReseniaService(
        ReseniaRepository repository,
        UsuarioRepository usuarioRepository,
        CocheRepository cocheRepository
    ) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.cocheRepository = cocheRepository;
    }

    private ReseniaResponse mapToResponse(Resenia resenia) {
        return new ReseniaResponse(
            resenia.getId(),
            resenia.getUsuario().getId(),
            resenia.getUsuario().getNombre(),
            resenia.getCoche().getId(),
            resenia.getCoche().getNomenclatura(),
            resenia.getPuntuacion(),
            resenia.getComentario(),
            resenia.getCreatedAt()
        );
    }

    private Resenia mapFromRequest(
        ReseniaRequest resenia,
        Usuario usuario,
        Coche coche
    ) {
        Resenia resena = new Resenia();
        resena.setCoche(coche);
        resena.setUsuario(usuario);
        resena.setPuntuacion(resenia.puntuacion());
        resena.setComentario(resenia.comentario().trim());
        return resena;
    }

    @Transactional(readOnly = true)
    public List<ReseniaResponse> findAll() {
        return repository
            .findAll()
            .stream()
            .map(resenia -> mapToResponse(resenia))
            .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ReseniaResponse> findById(UUID id) {
        Optional<ReseniaResponse> resenia = repository
            .findById(id)
            .map(res -> mapToResponse(res));
        return resenia;
    }

    @Transactional(readOnly = true)
    public List<ReseniaResponse> findByCocheId(UUID cocheId) {
        return repository
            .findByCocheId(cocheId)
            .stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ReseniaResponse> findByUsuarioId(UUID usuarioId) {
        List<Resenia> resenias = repository.findByUsuarioId(usuarioId);

        return resenias.stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResumenReseniasResponse getResumenByCocheId(UUID cocheId) {
        List<Resenia> resenias = repository.findByCocheId(cocheId);
        double media = resenias
            .stream()
            .mapToInt(Resenia::getPuntuacion)
            .average()
            .orElse(0.0);

        return new ResumenReseniasResponse(cocheId, media, resenias.size());
    }

    @Transactional(readOnly = false)
    public ReseniaResponse create(ReseniaRequest request) {
        Usuario usuario = usuarioRepository
            .findById(request.usuarioId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Usuario no encontrado")
            );
        Coche coche = cocheRepository
            .findById(request.cocheId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Coche no encontrado")
            );

        if (
            repository.existsByUsuarioIdAndCocheId(
                usuario.getId(),
                coche.getId()
            )
        ) {
            throw new BadRequestException(
                "El usuario ya ha publicado una resenia para este coche"
            );
        }

        Resenia resenia = mapFromRequest(request, usuario, coche);
        return mapToResponse(repository.save(resenia));
    }

    @Transactional(readOnly = false)
    public ReseniaResponse update(UUID id, ReseniaRequest request) {
        Resenia existente = repository
            .findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException("Resenia no encontrada")
            );

        Usuario usuario = usuarioRepository
            .findById(request.usuarioId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Usuario no encontrado")
            );
        Coche coche = cocheRepository
            .findById(request.cocheId())
            .orElseThrow(() ->
                new ResourceNotFoundException("Coche no encontrado")
            );

        boolean existeDuplicada = repository
            .findByUsuarioId(usuario.getId())
            .stream()
            .anyMatch(
                resenia ->
                    !resenia.getId().equals(id) &&
                    resenia.getCoche().getId().equals(coche.getId())
            );

        if (existeDuplicada) {
            throw new BadRequestException(
                "El usuario ya ha publicado una resenia para este coche"
            );
        }

        existente.setUsuario(usuario);
        existente.setCoche(coche);
        existente.setPuntuacion(request.puntuacion());
        existente.setComentario(request.comentario().trim());
        return mapToResponse(repository.save(existente));
    }

    @Transactional(readOnly = false)
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Resenia no encontrada");
        }
        repository.deleteById(id);
    }
}
