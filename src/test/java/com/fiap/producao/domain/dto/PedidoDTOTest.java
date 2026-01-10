package com.fiap.producao.domain.dto;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PedidoDTOTest {

    @Test
    void shouldConvertFromEntityToDTO() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        PedidoProducao entity = PedidoProducao.builder()
                .id("65f2d5de")
                .idPedidoOriginal(123L)
                .itens(Collections.emptyList())
                .status(StatusPedido.RECEBIDO) // Ajuste conforme seu Enum real
                .dataEntrada(now)
                .build();

        // Act
        PedidoDTO dto = PedidoDTO.fromEntity(entity);

        // Assert
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getIdPedidoOriginal(), dto.getIdPedidoOriginal());
        assertEquals(entity.getItens(), dto.getItens());
        assertEquals(entity.getStatus(), dto.getStatus());
        assertEquals(entity.getDataEntrada(), dto.getDataEntrada());
    }

    @Test
    void shouldTestLombokMethods() {
        // Teste simples para cobrir @Data, @Builder, @AllArgsConstructor
        PedidoDTO dto = new PedidoDTO();
        dto.setId("1");
        dto.setIdPedidoOriginal(1L);

        PedidoDTO dtoBuilder = PedidoDTO.builder()
                .id("1")
                .idPedidoOriginal(1L)
                .build();

        assertEquals(dto.getId(), dtoBuilder.getId());
    }
}