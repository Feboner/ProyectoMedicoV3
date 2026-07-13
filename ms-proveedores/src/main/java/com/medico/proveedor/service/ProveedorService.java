package com.medico.proveedor.service;

import com.medico.proveedor.model.Proveedor;
import com.medico.proveedor.repository.ProveedorRepository;
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
public class ProveedorService {
    private final ProveedorRepository repository;

    public Proveedor save(Proveedor entity) {
        log.debug("Saving {}",entity.getClass().getSimpleName());
        return repository.save(entity);
    }

    public Optional<Proveedor> findById(Long id) {
        return repository.findById(id);
    }

    public List<Proveedor> findAll() {
        return repository.findAll();
    }

    public Proveedor update(Proveedor entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
