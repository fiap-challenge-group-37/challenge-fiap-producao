package com.fiap.producao.domain.dto;

import com.fiap.producao.domain.entity.ItemProducao;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoDTOTest {

    @Test
    void testeCoberturaTotal() {
        // 1. Preparar dados
        String id = "123";
        Long idOriginal = 99L;
        StatusPedido status = StatusPedido.RECEBIDO; // Ajuste se seu enum for diferente
        LocalDateTime agora = LocalDateTime.now();
        List<ItemProducao> itens = new ArrayList<>();

        // 2. Testar Construtor @NoArgsConstructor e Setters (@Data)
        PedidoDTO dto1 = new PedidoDTO();
        dto1.setId(id);
        dto1.setIdPedidoOriginal(idOriginal);
        dto1.setStatus(status);
        dto1.setDataEntrada(agora);
        dto1.setItens(itens);

        // 3. Testar Getters (@Data)
        assertEquals(id, dto1.getId());
        assertEquals(idOriginal, dto1.getIdPedidoOriginal());
        assertEquals(status, dto1.getStatus());
        assertEquals(agora, dto1.getDataEntrada());
        assertEquals(itens, dto1.getItens());

        // 4. Testar @Builder e @AllArgsConstructor
        PedidoDTO dto2 = PedidoDTO.builder()
                .id(id)
                .idPedidoOriginal(idOriginal)
                .status(status)
                .dataEntrada(agora)
                .itens(itens)
                .build();

        // 5. Testar equals(), hashCode() e toString() (Gerados pelo @Data)
        // Isso é o que geralmente mata a cobertura do Lombok se não testar
        assertEquals(dto1, dto2); // Testar equals
        assertEquals(dto1.hashCode(), dto2.hashCode()); // Testar hashCode
        assertNotNull(dto1.toString()); // Testar toString

        // Testar desigualdade
        PedidoDTO dto3 = new PedidoDTO();
        assertNotEquals(dto1, dto3);
    }

    @Test
    void testeMetodoEstaticoFromEntity() {
        // Arrange
        PedidoProducao entity = new PedidoProducao();
        // Assumindo que PedidoProducao tem setters ou @Data.
        // Se não tiver, use o construtor ou builder dela.
        entity.setId("abc");
        entity.setIdPedidoOriginal(50L);
        entity.setStatus(StatusPedido.PRONTO); // Ajuste o enum se necessário
        entity.setDataEntrada(LocalDateTime.now());
        entity.setItens(new ArrayList<>());

        // Act
        PedidoDTO result = PedidoDTO.fromEntity(entity);

        // Assert
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getIdPedidoOriginal(), result.getIdPedidoOriginal());
        assertEquals(entity.getStatus(), result.getStatus());
    }
}