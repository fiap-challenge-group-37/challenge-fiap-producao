package com.fiap.producao.domain.dto;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PedidoDTOTest {

    @Test
    void deveConverterEntityParaDTO() {
        // 1. Prepara os dados (Arrange)
        PedidoProducao entity = PedidoProducao.builder()
                .id("12345")
                .idPedidoOriginal(99L)
                .itens(new ArrayList<>()) // Lista vazia só pra não quebrar
                .status(StatusPedido.RECEBIDO) // Ajuste se seu Enum for diferente
                .dataEntrada(LocalDateTime.now())
                .build();

        // 2. Roda o método problemático (Act)
        // AQUI É O PULO DO GATO: Chamar o fromEntity é o que dá o coverage
        PedidoDTO dto = PedidoDTO.fromEntity(entity);

        // 3. Valida (Assert)
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getIdPedidoOriginal(), dto.getIdPedidoOriginal());
        assertEquals(entity.getStatus(), dto.getStatus());
    }

    @Test
    void deveTestarConstrutoresLombok() {
        // Esse teste garante que o @Data e @Builder contem como cobertos também
        PedidoDTO dto = new PedidoDTO();
        dto.setId("teste");

        PedidoDTO dto2 = PedidoDTO.builder().id("teste").build();

        assertEquals(dto.getId(), dto2.getId());
    }
}