package com.medico.receta.service;

import com.medico.receta.client.PacienteClient;
import com.medico.receta.model.Receta;
import com.medico.receta.repository.RecetaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RecetaService {
    private final RecetaRepository repository;
    private final PacienteClient pacienteClient;

    public Receta save(Receta entity) {
        log.debug("Saving {}",entity.getClass().getSimpleName());
        if (entity.getPacienteId() == null || !pacienteClient.existsById(entity.getPacienteId())) {
            throw new IllegalArgumentException("No se puede crear la receta porque el paciente no existe");
        }
        return repository.save(entity);
    }

    public Optional<Receta> findById(Long id) {
        return repository.findById(id);
    }

    public List<Receta> findAll() {
        return repository.findAll();
    }

    public Receta update(Receta entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
