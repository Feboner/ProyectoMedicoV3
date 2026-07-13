package com.medico.pedido.service;

import com.medico.pedido.client.PacienteClient;
import com.medico.pedido.model.Pedido;
import com.medico.pedido.repository.PedidoRepository;
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
public class PedidoService {
    private final PedidoRepository repository;
    private final PacienteClient pacienteClient;

    public Pedido save(Pedido entity) {
        log.debug("Saving {}",entity.getClass().getSimpleName());
        if (entity.getPacienteId() == null || !pacienteClient.existsById(entity.getPacienteId())) {
            throw new IllegalArgumentException("No se puede crear el pedido porque el paciente no existe");
        }
        return repository.save(entity);
    }

    public Optional<Pedido> findById(Long id) {
        return repository.findById(id);
    }

    public List<Pedido> findAll() {
        return repository.findAll();
    }

    public Pedido update(Pedido entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}