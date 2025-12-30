package com.fiap.challenge.cliente.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String testSecret = "test-secret-key-that-is-long-enough-for-HS256-algorithm-requirements";
    private final Long testExpiration = 3600000L; // 1 hour

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(testSecret, testExpiration);
    }

    @Test
    @DisplayName("Deve gerar token JWT com sucesso para usuário comum")
    void deveGerarTokenComSucesso() {
        String cpf = "12345678901";
        List<String> roles = Arrays.asList("ROLE_USER");

        String token = jwtService.generateToken(cpf, roles);

        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.contains("."));
    }

    @Test
    @DisplayName("Deve gerar token JWT sem expiração para admin")
    void deveGerarTokenSemExpiracaoParaAdmin() {
        String cpf = "12345678901";
        List<String> roles = Arrays.asList("ROLE_ADMIN");

        String token = jwtService.generateToken(cpf, roles);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    @DisplayName("Deve gerar token com roles nulas")
    void deveGerarTokenComRolesNulas() {
        String cpf = "12345678901";

        String token = jwtService.generateToken(cpf, null);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    @DisplayName("Deve validar token JWT válido")
    void deveValidarTokenValido() {
        String cpf = "12345678901";
        List<String> roles = Arrays.asList("ROLE_USER");
        String token = jwtService.generateToken(cpf, roles);

        boolean isValid = jwtService.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Deve retornar false para token inválido")
    void deveRetornarFalseParaTokenInvalido() {
        String invalidToken = "token.invalido.aqui";

        boolean isValid = jwtService.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Deve extrair CPF do token")
    void deveExtrairCpfDoToken() {
        String cpf = "12345678901";
        List<String> roles = Arrays.asList("ROLE_USER");
        String token = jwtService.generateToken(cpf, roles);

        String extractedCpf = jwtService.extractCpf(token);

        assertEquals(cpf, extractedCpf);
    }

    @Test
    @DisplayName("Deve extrair roles do token")
    void deveExtrairRolesDoToken() {
        String cpf = "12345678901";
        List<String> roles = Arrays.asList("ROLE_USER", "ROLE_CUSTOMER");
        String token = jwtService.generateToken(cpf, roles);

        List<String> extractedRoles = jwtService.extractRoles(token);

        assertNotNull(extractedRoles);
        assertEquals(2, extractedRoles.size());
        assertTrue(extractedRoles.contains("ROLE_USER"));
        assertTrue(extractedRoles.contains("ROLE_CUSTOMER"));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando roles não estão no token")
    void deveRetornarListaVaziaQuandoRolesNaoEstaoNoToken() {
        String cpf = "12345678901";
        String token = jwtService.generateToken(cpf, Collections.emptyList());

        List<String> extractedRoles = jwtService.extractRoles(token);

        assertNotNull(extractedRoles);
        assertEquals(0, extractedRoles.size());
    }
}