package com.medico.envio.service;

import com.medico.envio.client.PedidoClient;
import com.medico.envio.model.Envio;
import com.medico.envio.repository.EnvioRepository;
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
public class EnvioService {
    private final EnvioRepository repository;
    private final PedidoClient pedidoClient;

    public Envio save(Envio entity) {
        log.debug("Saving {}",entity.getClass().getSimpleName());
        if (entity.getPedidoId() == null || !pedidoClient.existsById(entity.getPedidoId())) {
            throw new IllegalArgumentException("No se puede crear el envío porque el pedido no existe");
        }
        return repository.save(entity);
    }

    public Optional<Envio> findById(Long id) {
        return repository.findById(id);
    }

    public List<Envio> findAll() {
        return repository.findAll();
    }

    public Envio update(Envio entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
