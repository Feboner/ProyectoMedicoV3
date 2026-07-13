package com.medico.receta.client;

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
public class PacienteClient {
    private final WebClient webClient;

    public boolean existsById(Long pacienteId) {
        if (pacienteId == null) {
            return false;
        }

        try {
            Boolean existe = webClient.get()
                    .uri("/api/v1/pacientes/{id}", pacienteId)
                    .retrieve()
                    .toBodilessEntity()
                    .map(response -> true)
                    .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.just(false))
                    .onErrorResume(WebClientRequestException.class, ex -> {
                        log.warn("No fue posible validar el paciente {}: {}", pacienteId, ex.getMessage());
                        return Mono.just(false);
                    })
                    .block();

            return Boolean.TRUE.equals(existe);
        } catch (Exception ex) {
            log.warn("Fallo inesperado al validar paciente {}: {}", pacienteId, ex.getMessage());
            return false;
        }
    }
}
