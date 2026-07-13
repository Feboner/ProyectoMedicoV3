package com.medico.envio.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoClient {
    private final WebClient webClient;

    public boolean existsById(Long pedidoId) {
        if (pedidoId == null) {
            return false;
        }

        try {
            Boolean existe = webClient.get()
                    .uri("/api/v1/pedidos/{id}", pedidoId)
                    .retrieve()
                    .toBodilessEntity()
                    .map(response -> true)
                    .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.just(false))
                    .onErrorResume(WebClientRequestException.class, ex -> {
                        log.warn("No fue posible validar el pedido {}: {}", pedidoId, ex.getMessage());
                        return Mono.just(false);
                    })
                    .block();

            return Boolean.TRUE.equals(existe);
        } catch (Exception ex) {
            log.warn("Fallo inesperado al validar pedido {}: {}", pedidoId, ex.getMessage());
            return false;
        }
    }
}
