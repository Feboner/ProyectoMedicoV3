package com.medico.inventarioMovimiento.service;

import com.medico.inventarioMovimiento.model.InventarioMovimiento;
import com.medico.inventarioMovimiento.repository.InventarioMovimientoRepository;
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
public class InventarioMovimientoService {
    private final InventarioMovimientoRepository repository;

    public InventarioMovimiento save(InventarioMovimiento entity) {
        log.debug("Saving {}",entity.getClass().getSimpleName());
        return repository.save(entity);
    }

    public Optional<InventarioMovimiento> findById(Long id) {
        return repository.findById(id);
    }

    public List<InventarioMovimiento> findAll() {
        return repository.findAll();
    }

    public InventarioMovimiento update(InventarioMovimiento entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}