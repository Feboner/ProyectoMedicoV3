package com.medico.pago.service;

import com.medico.pago.model.Pago;
import com.medico.pago.repository.PagoRepository;
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
public class PagoService {
    private final PagoRepository repository;

    public Pago save(Pago entity) {
        log.debug("Saving {}",entity.getClass().getSimpleName());
        return repository.save(entity);
    }

    public Optional<Pago> findById(Long id) {
        return repository.findById(id);
    }

    public List<Pago> findAll() {
        return repository.findAll();
    }

    public Pago update(Pago entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
