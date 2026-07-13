package com.medico.producto.service;

import com.medico.producto.model.Producto;
import com.medico.producto.repository.ProductoRepository;
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
public class ProductoService {
    private final ProductoRepository repository;

    public Producto save(Producto entity) {
        log.debug("Saving {}",entity.getClass().getSimpleName());
        return repository.save(entity);
    }

    public Optional<Producto> findById(Long id) {
        return repository.findById(id);
    }

    public List<Producto> findAll() {
        return repository.findAll();
    }

    public Producto update(Producto entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}