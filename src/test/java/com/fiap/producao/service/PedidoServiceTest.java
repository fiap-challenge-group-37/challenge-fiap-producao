package com.fiap.producao.service;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        // Criação manual sem anotações para garantir instrumentação limpa
        repository = Mockito.mock(PedidoProducaoRepository.class);
        service = new PedidoService(repository);
    }

    @Test
    void deveListarApenasPedidosNaoFinalizados() {
        PedidoProducao p1 = PedidoProducao.builder().status(StatusPedido.RECEBIDO).build();
        
        when(repository.findByStatusNot(StatusPedido.FINALIZADO))
                .thenReturn(List.of(p1));

        List<PedidoProducao> resultado = service.listarFilaCozinha();

        Assertions.assertFalse(resultado.isEmpty());
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(StatusPedido.RECEBIDO, resultado.get(0).getStatus());
        
        verify(repository, times(1)).findByStatusNot(StatusPedido.FINALIZADO);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverPedidos() {
        when(repository.findByStatusNot(any())).thenReturn(Collections.emptyList());

        List<PedidoProducao> resultado = service.listarFilaCozinha();

        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.isEmpty());
    }
}