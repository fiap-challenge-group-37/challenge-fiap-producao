package com.fiap.producao.integration;

import com.fiap.producao.domain.entity.StatusPedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class PedidoIntegrationService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoIntegrationService.class);
    private final WebClient webClient;

    public PedidoIntegrationService(WebClient.Builder webClientBuilder,
                                    @Value("${integration.pedidos.url}") String pedidosUrl) {
        this.webClient = webClientBuilder.baseUrl(pedidosUrl).build();
    }

    public void atualizarStatusNoMsPedidos(Long idPedidoOriginal, StatusPedido novoStatus) {
        try {
            String token = getTokenAtual();

            webClient.patch()
                    .uri("/api/pedidos/{id}/status", idPedidoOriginal)
                    .headers(h -> h.setBearerAuth(token))
                    // AQUI ESTÁ A CORREÇÃO: Chave "novoStatus"
                    .bodyValue(Map.of("novoStatus", novoStatus.name()))
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            logger.info("Pedido {} atualizado para {} no MS Pedidos.", idPedidoOriginal, novoStatus);
        } catch (Exception e) {
            logger.error("Erro na integração com Pedidos: {}", e.getMessage());
        }
    }

    private String getTokenAtual() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt jwt) {
            return jwt.getTokenValue();
        }
        return "";
    }
}