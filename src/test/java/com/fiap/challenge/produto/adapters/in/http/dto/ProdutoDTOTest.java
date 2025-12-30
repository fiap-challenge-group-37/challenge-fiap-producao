package com.fiap.challenge.produto.adapters.in.http.dto;

import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoDTOTest {

    @Test
    @DisplayName("Deve criar ProdutoDTO com construtor padrão")
    void deveCriarProdutoDTOComConstrutorPadrao() {
        ProdutoDTO dto = new ProdutoDTO();
        
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Deve criar ProdutoDTO com construtor completo")
    void deveCriarProdutoDTOComConstrutorCompleto() {
        ProdutoDTO dto = new ProdutoDTO(1L, "Hamburguer", "LANCHE", BigDecimal.valueOf(25.50), "Hamburguer delicioso");
        
        assertEquals(1L, dto.getId());
        assertEquals("Hamburguer", dto.getNome());
        assertEquals("LANCHE", dto.getCategoria());
        assertEquals(BigDecimal.valueOf(25.50), dto.getPreco());
        assertEquals("Hamburguer delicioso", dto.getDescricao());
    }

    @Test
    @DisplayName("Deve usar setters e getters corretamente")
    void deveUsarSettersEGettersCorretamente() {
        ProdutoDTO dto = new ProdutoDTO();
        
        dto.setId(2L);
        dto.setNome("Batata Frita");
        dto.setCategoria("ACOMPANHAMENTO");
        dto.setPreco(BigDecimal.valueOf(8.00));
        dto.setDescricao("Batata crocante");
        
        assertEquals(2L, dto.getId());
        assertEquals("Batata Frita", dto.getNome());
        assertEquals("ACOMPANHAMENTO", dto.getCategoria());
        assertEquals(BigDecimal.valueOf(8.00), dto.getPreco());
        assertEquals("Batata crocante", dto.getDescricao());
    }

    @Test
    @DisplayName("Deve converter de domínio para DTO com sucesso")
    void deveConverterDeDominioParaDTOComSucesso() {
        Produto produto = new Produto(1L, "Hamburguer", Categoria.LANCHE, BigDecimal.valueOf(25.50), "Delicioso");
        
        ProdutoDTO dto = ProdutoDTO.fromDomain(produto);
        
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Hamburguer", dto.getNome());
        assertEquals("LANCHE", dto.getCategoria());
        assertEquals(BigDecimal.valueOf(25.50), dto.getPreco());
        assertEquals("Delicioso", dto.getDescricao());
    }

    @Test
    @DisplayName("Deve retornar null quando produto é null no fromDomain")
    void deveRetornarNullQuandoProdutoNullNoFromDomain() {
        ProdutoDTO dto = ProdutoDTO.fromDomain(null);
        
        assertNull(dto);
    }

    @Test
    @DisplayName("Deve converter de DTO para domínio com sucesso - com ID")
    void deveConverterDeDTOParaDominioComSucessoComId() {
        ProdutoDTO dto = new ProdutoDTO(1L, "Hamburguer", "LANCHE", BigDecimal.valueOf(25.50), "Delicioso");
        
        Produto produto = dto.toDomain();
        
        assertNotNull(produto);
        assertEquals(1L, produto.getId());
        assertEquals("Hamburguer", produto.getNome());
        assertEquals(Categoria.LANCHE, produto.getCategoria());
        assertEquals(BigDecimal.valueOf(25.50), produto.getPreco());
        assertEquals("Delicioso", produto.getDescricao());
    }

    @Test
    @DisplayName("Deve converter de DTO para domínio com sucesso - sem ID")
    void deveConverterDeDTOParaDominioComSucessoSemId() {
        ProdutoDTO dto = new ProdutoDTO(null, "Hamburguer", "LANCHE", BigDecimal.valueOf(25.50), "Delicioso");
        
        Produto produto = dto.toDomain();
        
        assertNotNull(produto);
        assertNull(produto.getId());
        assertEquals("Hamburguer", produto.getNome());
        assertEquals(Categoria.LANCHE, produto.getCategoria());
        assertEquals(BigDecimal.valueOf(25.50), produto.getPreco());
        assertEquals("Delicioso", produto.getDescricao());
    }
}