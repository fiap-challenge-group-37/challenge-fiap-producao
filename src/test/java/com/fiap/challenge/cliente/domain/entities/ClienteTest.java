package com.fiap.challenge.cliente.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    @DisplayName("Deve lançar exceção quando CPF for inválido")
    void deveLancarExcecaoQuandoCpfInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cliente("12345678901", "Nome Teste", "teste@email.com", "ROLE_CLIENTE"));
    }

    @Test
    @DisplayName("Deve criar cliente com CPF válido")
    void deveCriarClienteComCpfValido() {
        Cliente cliente = new Cliente("52998224725", "João", "joao@email.com", "ROLE_CLIENTE");
        assertEquals("João", cliente.getNome());
        assertEquals("joao@email.com", cliente.getEmail());
        assertEquals("52998224725", cliente.getCpf().getValue());
        assertEquals("ROLE_CLIENTE", cliente.getRole());
    }

    @Test
    @DisplayName("Deve atualizar nome do cliente")
    void deveAtualizarNomeDoCliente() {
        Cliente cliente = new Cliente("52998224725", "João", "joao@email.com", "ROLE_CLIENTE");
        
        cliente.setNome("João Silva");
        
        assertEquals("João Silva", cliente.getNome());
    }

    @Test
    @DisplayName("Deve atualizar email do cliente")
    void deveAtualizarEmailDoCliente() {
        Cliente cliente = new Cliente("52998224725", "João", "joao@email.com", "ROLE_CLIENTE");
        
        cliente.setEmail("joao.silva@email.com");
        
        assertEquals("joao.silva@email.com", cliente.getEmail());
    }

    @Test
    @DisplayName("Deve atualizar role do cliente")
    void deveAtualizarRoleDoCliente() {
        Cliente cliente = new Cliente("52998224725", "João", "joao@email.com", "ROLE_CLIENTE");
        
        cliente.setRole("ROLE_ADMIN");
        
        assertEquals("ROLE_ADMIN", cliente.getRole());
    }

    @Test
    @DisplayName("Deve atualizar CPF do cliente")
    void deveAtualizarCpfDoCliente() {
        Cliente cliente = new Cliente("52998224725", "João", "joao@email.com", "ROLE_CLIENTE");
        Cpf novoCpf = new Cpf("11144477735");
        
        cliente.setCpf(novoCpf);
        
        assertEquals("11144477735", cliente.getCpf().getValue());
    }
}
