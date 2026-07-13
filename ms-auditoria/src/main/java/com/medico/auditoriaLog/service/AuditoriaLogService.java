package com.medico.auditoriaLog.service;

import com.medico.auditoriaLog.model.AuditoriaLog;
import com.medico.auditoriaLog.repository.AuditoriaLogRepository;
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
public class AuditoriaLogService {
    private final AuditoriaLogRepository repository;

    public AuditoriaLog save(AuditoriaLog entity) {
        log.debug("Saving {}",entity.getClass().getSimpleName());
        return repository.save(entity);
    }

    public Optional<AuditoriaLog> findById(Long id) {
        return repository.findById(id);
    }

    public List<AuditoriaLog> findAll() {
        return repository.findAll();
    }

    public AuditoriaLog update(AuditoriaLog entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
