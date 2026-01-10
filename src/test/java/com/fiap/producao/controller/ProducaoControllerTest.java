package com.fiap.producao.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.producao.domain.dto.StatusDTO;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.domain.dto.PedidoDTO; // Importe o DTO que criamos
import com.fiap.producao.integration.PedidoIntegrationService; // Importe o Service de Integração
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProducaoController.class)
class ProducaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ADICIONADO: O Controller precisa desse Mock para funcionar
    @MockBean
    private PedidoIntegrationService pedidoIntegrationService;

    @MockBean
    private PedidoProducaoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarFila() throws Exception {
        // Arrange
        PedidoProducao pedido = PedidoProducao.builder()
                .id("1")
                .status(StatusPedido.RECEBIDO)
                .dataEntrada(LocalDateTime.now()) // Importante para evitar NullPointer no DTO
                .build();

        when(repository.findAll()).thenReturn(List.of(pedido));

        // Act & Assert
        mockMvc.perform(get("/producao/fila"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void deveAtualizarStatusComSucesso() throws Exception {
        // Arrange
        PedidoProducao pedido = PedidoProducao.builder()
                .id("1")
                .idPedidoOriginal(100L) // Necessário para a integração
                .status(StatusPedido.RECEBIDO)
                .dataEntrada(LocalDateTime.now())
                .build();

        when(repository.findById("1")).thenReturn(Optional.of(pedido));
        when(repository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        StatusDTO dto = new StatusDTO(StatusPedido.EM_PREPARACAO);

        // Act & Assert
        mockMvc.perform(patch("/producao/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_PREPARACAO"));
    }

    @Test
    void deveRetornar404QuandoPedidoNaoEncontrado() throws Exception {
        // Arrange
        when(repository.findById("999")).thenReturn(Optional.empty());
        StatusDTO dto = new StatusDTO(StatusPedido.PRONTO);

        // Act & Assert
        mockMvc.perform(patch("/producao/999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}