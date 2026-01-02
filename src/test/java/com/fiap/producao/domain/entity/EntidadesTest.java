package com.fiap.producao.domain.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EntidadesTest {

    @Test
    void deveTestarItemProducao() {
        ItemProducao item = new ItemProducao();
        item.setNome("Burger");
        item.setQuantidade(1);

        assertEquals("Burger", item.getNome());
        assertEquals(1, item.getQuantidade());
        
        ItemProducao item2 = new ItemProducao("Burger", 1);
        assertEquals(item.getNome(), item2.getNome());
    }

    @Test
    void deveTestarPedidoProducao() {
        LocalDateTime agora = LocalDateTime.now();
        PedidoProducao pedido = new PedidoProducao();
        pedido.setId("123");
        pedido.setIdPedidoOriginal(10L);
        pedido.setStatus(StatusPedido.RECEBIDO);
        pedido.setDataEntrada(agora);
        pedido.setDataAtualizacao(agora);
        pedido.setItens(List.of(new ItemProducao("Fritas", 1)));

        assertEquals("123", pedido.getId());
        assertEquals(10L, pedido.getIdPedidoOriginal());
        assertEquals(StatusPedido.RECEBIDO, pedido.getStatus());
        assertEquals(agora, pedido.getDataEntrada());
        assertNotNull(pedido.getItens());
        
        PedidoProducao pedido2 = PedidoProducao.builder()
            .id("123")
            .status(StatusPedido.RECEBIDO)
            .build();
        assertNotNull(pedido2);
    }
}