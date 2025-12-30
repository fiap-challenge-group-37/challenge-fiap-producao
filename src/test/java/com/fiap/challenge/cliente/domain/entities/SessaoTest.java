package com.fiap.challenge.cliente.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SessaoTest {

    @Test
    @DisplayName("Deve criar sessão com construtor padrão")
    void deveCriarSessaoComConstrutorPadrao() {
        Sessao sessao = new Sessao();
        
        assertNotNull(sessao);
    }

    @Test
    @DisplayName("Deve definir e obter ID corretamente")
    void deveDefinirEObterIdCorretamente() {
        Sessao sessao = new Sessao();
        Long id = 1L;
        
        sessao.setId(id);
        
        assertEquals(id, sessao.getId());
    }

    @Test
    @DisplayName("Deve definir e obter CPF corretamente")
    void deveDefinirEObterCpfCorretamente() {
        Sessao sessao = new Sessao();
        String cpf = "12345678901";
        
        sessao.setCpf(cpf);
        
        assertEquals(cpf, sessao.getCpf());
    }

    @Test
    @DisplayName("Deve definir e obter token corretamente")
    void deveDefinirEObterTokenCorretamente() {
        Sessao sessao = new Sessao();
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        
        sessao.setToken(token);
        
        assertEquals(token, sessao.getToken());
    }

    @Test
    @DisplayName("Deve definir e obter data de criação corretamente")
    void deveDefinirEObterDataCriacaoCorretamente() {
        Sessao sessao = new Sessao();
        Date dataCriacao = new Date();
        
        sessao.setDataCriacao(dataCriacao);
        
        assertEquals(dataCriacao, sessao.getDataCriacao());
    }

    @Test
    @DisplayName("Deve aceitar valores nulos nos campos")
    void deveAceitarValoresNulosNosCampos() {
        Sessao sessao = new Sessao();
        
        sessao.setId(null);
        sessao.setCpf(null);
        sessao.setToken(null);
        sessao.setDataCriacao(null);
        
        assertNull(sessao.getId());
        assertNull(sessao.getCpf());
        assertNull(sessao.getToken());
        assertNull(sessao.getDataCriacao());
    }
}