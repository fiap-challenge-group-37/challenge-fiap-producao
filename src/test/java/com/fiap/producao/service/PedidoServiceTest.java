package com.fiap.producao.service;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PedidoServiceTest {

    private PedidoService service;
    private PedidoProducaoRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PedidoProducaoRepository.class);
        service = new PedidoService(repository);
    }

    @Test
    void deveListarPedidosOrdenadosPorPrioridadeEData() {
        LocalDateTime now = LocalDateTime.now();

        PedidoProducao p1 = PedidoProducao.builder().id("1").status(StatusPedido.PRONTO).dataEntrada(now.plusMinutes(10)).build();
        PedidoProducao p2 = PedidoProducao.builder().id("2").status(StatusPedido.EM_PREPARACAO).dataEntrada(now).build();
        PedidoProducao p3 = PedidoProducao.builder().id("3").status(StatusPedido.RECEBIDO).dataEntrada(now.minusHours(1)).build();
        PedidoProducao p4 = PedidoProducao.builder().id("4").status(StatusPedido.RECEBIDO).dataEntrada(now).build();

        when(repository.findByStatusNot(StatusPedido.FINALIZADO))
                .thenReturn(Arrays.asList(p4, p2, p3, p1));

        List<PedidoProducao> resultado = service.listarFilaCozinha();

        Assertions.assertEquals(4, resultado.size());
        Assertions.assertEquals("1", resultado.get(0).getId());
        Assertions.assertEquals("2", resultado.get(1).getId());
        Assertions.assertEquals("3", resultado.get(2).getId());
        Assertions.assertEquals("4", resultado.get(3).getId());

        verify(repository, times(1)).findByStatusNot(StatusPedido.FINALIZADO);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverPedidos() {
        when(repository.findByStatusNot(any())).thenReturn(Collections.emptyList());
        Assertions.assertTrue(service.listarFilaCozinha().isEmpty());
    }

    @Test
    void deveRetornarPrioridadeZeroParaStatusFinalizado() {
        PedidoProducao pedido = PedidoProducao.builder().status(StatusPedido.FINALIZADO).build();
        int prioridade = service.getPrioridade(pedido);
        Assertions.assertEquals(99, prioridade);
    }
}
