package com.fiap.producao.domain.dto;

import com.fiap.producao.domain.entity.ItemProducao;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoDTOTest {

    @Test
    void deveCriarDtoViaBuilderEConstrutor() {
        // Arrange
        String id = "uuid-123";
        Long idOriginal = 10L;
        StatusPedido status = StatusPedido.RECEBIDO; // Ajuste conforme seu Enum real
        LocalDateTime agora = LocalDateTime.now();
        List<ItemProducao> itens = Collections.emptyList();

        // Act - Testando Builder e AllArgsConstructor
        PedidoDTO dto = PedidoDTO.builder()
                .id(id)
                .idPedidoOriginal(idOriginal)
                .status(status)
                .dataEntrada(agora)
                .itens(itens)
                .build();

        // Assert - Testando Getters
        assertEquals(id, dto.getId());
        assertEquals(idOriginal, dto.getIdPedidoOriginal());
        assertEquals(status, dto.getStatus());
        assertEquals(agora, dto.getDataEntrada());
        assertEquals(itens, dto.getItens());
    }

    @Test
    void deveCriarDtoViaNoArgsESetters() {
        // Testando NoArgsConstructor e Setters
        PedidoDTO dto = new PedidoDTO();
        dto.setId("123");
        dto.setStatus(StatusPedido.PRONTO); // Ajuste conforme seu Enum

        assertEquals("123", dto.getId());
        assertEquals(StatusPedido.PRONTO, dto.getStatus());

        // Testando toString() e equals/hashCode (gerados pelo @Data)
        // Isso é essencial para 100% de coverage no Lombok
        assertNotNull(dto.toString());
        PedidoDTO dto2 = new PedidoDTO();
        dto2.setId("123");
        dto2.setStatus(StatusPedido.PRONTO);
        assertEquals(dto, dto2);
        assertEquals(dto.hashCode(), dto2.hashCode());
    }

    @Test
    void deveConverterDeEntityParaDtoCorretamente() {
        // Arrange
        String id = "uuid-entity";
        Long idOriginal = 99L;
        StatusPedido status = StatusPedido.EM_PREPARACAO;
        LocalDateTime data = LocalDateTime.now();
        List<ItemProducao> itens = Collections.emptyList();

        // Simulando a Entity (Como não tenho o código da Entity, estou usando setters ou construtor)
        // Se sua entity usar Builder, ajuste aqui.
        PedidoProducao entity = new PedidoProducao();
        entity.setId(id);
        entity.setIdPedidoOriginal(idOriginal);
        entity.setStatus(status);
        entity.setDataEntrada(data);
        entity.setItens(itens);

        // Act - AQUI ESTÁ O SEGREDO DA COBERTURA DO MÉTODO fromEntity
        PedidoDTO result = PedidoDTO.fromEntity(entity);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(idOriginal, result.getIdPedidoOriginal());
        assertEquals(status, result.getStatus());
        assertEquals(data, result.getDataEntrada());
        assertEquals(itens, result.getItens());
    }
}