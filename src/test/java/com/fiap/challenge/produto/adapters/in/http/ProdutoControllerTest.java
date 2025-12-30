package com.fiap.challenge.produto.adapters.in.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.challenge.config.SecurityConfig;
import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.challenge.produto.application.port.in.*;
import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
@Import({SecurityConfig.class})
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CriarProdutoUseCase criarProdutoUseCase;

    @MockBean
    private AtualizarProdutoUseCase atualizarProdutoUseCase;

    @MockBean
    private RemoverProdutoUseCase removerProdutoUseCase;

    @MockBean
    private BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;

    @MockBean
    private BuscarProdutoPorCategoriaUseCase buscarProdutoPorCategoriaUseCase;

    @MockBean
    private ListarTodosProdutosUseCase listarTodosProdutosUseCase;

    @Test
    @DisplayName("Deve criar produto com sucesso - Admin")
    @WithMockUser(roles = "ADMIN")
    void deveCriarProdutoComSucesso() throws Exception {
        ProdutoDTO inputDTO = new ProdutoDTO(null, "Hamburguer", "LANCHE", BigDecimal.valueOf(25.50), "Hamburguer delicioso");
        Produto produtoSalvo = new Produto(1L, "Hamburguer", Categoria.LANCHE, BigDecimal.valueOf(25.50), "Hamburguer delicioso");

        when(criarProdutoUseCase.executar(any(ProdutoDTO.class))).thenReturn(produtoSalvo);

        mockMvc.perform(post("/produtos")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Hamburguer"))
                .andExpect(jsonPath("$.categoria").value("LANCHE"))
                .andExpect(jsonPath("$.preco").value(25.50));

        verify(criarProdutoUseCase, times(1)).executar(any(ProdutoDTO.class));
    }

    @Test
    @DisplayName("Deve editar produto com sucesso - Admin")
    @WithMockUser(roles = "ADMIN")
    void deveEditarProdutoComSucesso() throws Exception {
        Long produtoId = 1L;
        ProdutoDTO inputDTO = new ProdutoDTO(produtoId, "Hamburguer Atualizado", "LANCHE", BigDecimal.valueOf(30.00), "Novo hamburguer");
        Produto produtoAtualizado = new Produto(produtoId, "Hamburguer Atualizado", Categoria.LANCHE, BigDecimal.valueOf(30.00), "Novo hamburguer");

        when(atualizarProdutoUseCase.executar(anyLong(), any(ProdutoDTO.class))).thenReturn(produtoAtualizado);

        mockMvc.perform(put("/produtos/{produto_id}", produtoId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Hamburguer Atualizado"))
                .andExpect(jsonPath("$.preco").value(30.00));

        verify(atualizarProdutoUseCase, times(1)).executar(eq(produtoId), any(ProdutoDTO.class));
    }

    @Test
    @DisplayName("Deve remover produto com sucesso - Admin")
    @WithMockUser(roles = "ADMIN")
    void deveRemoverProdutoComSucesso() throws Exception {
        Long produtoId = 1L;

        doNothing().when(removerProdutoUseCase).removerPorId(produtoId);

        mockMvc.perform(delete("/produtos/{produto_id}", produtoId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(removerProdutoUseCase, times(1)).removerPorId(produtoId);
    }

    @Test
    @DisplayName("Deve listar todos os produtos com sucesso")
    @WithMockUser
    void deveListarTodosProdutosComSucesso() throws Exception {
        List<Produto> produtos = Arrays.asList(
                new Produto(1L, "Hamburguer", Categoria.LANCHE, BigDecimal.valueOf(25.50), "Desc1"),
                new Produto(2L, "Batata Frita", Categoria.ACOMPANHAMENTO, BigDecimal.valueOf(8.00), "Desc2")
        );

        when(listarTodosProdutosUseCase.executar()).thenReturn(produtos);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Hamburguer"))
                .andExpect(jsonPath("$[1].nome").value("Batata Frita"));

        verify(listarTodosProdutosUseCase, times(1)).executar();
    }

    @Test
    @DisplayName("Deve buscar produtos por categoria com sucesso")
    @WithMockUser
    void deveBuscarProdutosPorCategoriaComSucesso() throws Exception {
        List<Produto> produtosDaCategoria = Arrays.asList(
                new Produto(1L, "Hamburguer", Categoria.LANCHE, BigDecimal.valueOf(25.50), "Desc1")
        );

        when(buscarProdutoPorCategoriaUseCase.executar(Categoria.LANCHE)).thenReturn(produtosDaCategoria);

        mockMvc.perform(get("/produtos")
                        .param("categoria", "LANCHE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].categoria").value("LANCHE"));

        verify(buscarProdutoPorCategoriaUseCase, times(1)).executar(Categoria.LANCHE);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há produtos")
    @WithMockUser
    void deveRetornarListaVaziaQuandoNaoHaProdutos() throws Exception {
        when(listarTodosProdutosUseCase.executar()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(listarTodosProdutosUseCase, times(1)).executar();
    }

    @Test
    @DisplayName("Deve buscar produto por ID com sucesso")
    @WithMockUser
    void deveBuscarProdutoPorIdComSucesso() throws Exception {
        Long produtoId = 1L;
        Produto produto = new Produto(produtoId, "Hamburguer", Categoria.LANCHE, BigDecimal.valueOf(25.50), "Desc");

        when(buscarProdutoPorIdUseCase.buscarPorId(produtoId)).thenReturn(produto);

        mockMvc.perform(get("/produtos/{produto_id}", produtoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Hamburguer"))
                .andExpect(jsonPath("$.categoria").value("LANCHE"));

        verify(buscarProdutoPorIdUseCase, times(1)).buscarPorId(produtoId);
    }
}