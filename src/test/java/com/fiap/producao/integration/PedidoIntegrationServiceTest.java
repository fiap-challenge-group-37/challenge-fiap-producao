package com.fiap.producao.integration;

import com.fiap.producao.domain.entity.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoIntegrationServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @Mock
    private Jwt jwt;

    private PedidoIntegrationService service;

    @BeforeEach
    void setUp() {
        // Simula a construção do WebClient no construtor do Service
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        service = new PedidoIntegrationService(webClientBuilder, "http://localhost:8080");
    }

    @Test
    void deveAtualizarStatusComSucesso() {
        // 1. Simula o Token JWT no Contexto de Segurança
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getTokenValue()).thenReturn("token-mock-123");
        SecurityContextHolder.setContext(securityContext);

        // 2. Simula o encadeamento do WebClient (patch -> uri -> headers -> body -> retrieve)
        when(webClient.patch()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestBodySpec);

        // Truque para cobrir a lambda dos headers (h -> h.setBearerAuth...)
        when(requestBodySpec.headers(any())).thenAnswer(invocation -> {
            Consumer<HttpHeaders> headersConsumer = invocation.getArgument(0);
            headersConsumer.accept(new HttpHeaders()); // Executa a lambda para contar cobertura
            return requestBodySpec;
        });

        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.just(ResponseEntity.ok().build()));

        // Act
        service.atualizarStatusNoMsPedidos(1L, StatusPedido.EM_PREPARACAO);

        // Assert
        verify(webClient).patch();
    }

    @Test
    void deveLogarErroQuandoIntegracaoFalhar() {
        // 1. Simula Token
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getTokenValue()).thenReturn("token-mock");
        SecurityContextHolder.setContext(securityContext);

        // 2. Simula Erro no WebClient
        when(webClient.patch()).thenThrow(new RuntimeException("Erro de conexão"));

        // Act
        service.atualizarStatusNoMsPedidos(1L, StatusPedido.PRONTO);

        // Assert - Não deve lançar exceção (pois o service captura e loga)
        verify(webClient).patch();
    }

    @Test
    void deveTratarCasoSemTokenJwt() {
        // Simula um principal que NÃO é JWT (ex: Anonymous)
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("anonymousUser"); // String, não Jwt
        SecurityContextHolder.setContext(securityContext);

        // Mock básico para não dar NullPointer antes da lógica do token
        when(webClient.patch()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(), any(Object.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.empty());

        // Act
        service.atualizarStatusNoMsPedidos(1L, StatusPedido.FINALIZADO);

        // Assert: O código deve rodar sem quebrar, usando token vazio ou falhando graciosamente
        verify(webClient).patch();
    }
}