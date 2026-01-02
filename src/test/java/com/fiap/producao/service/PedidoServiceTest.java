package com.fiap.producao.service;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @InjectMocks
    private PedidoService service;

    @Mock
    private PedidoProducaoRepository repository;

    @Test
    void deveListarApenasPedidosNaoFinalizados() {
        PedidoProducao p1 = PedidoProducao.builder().status(StatusPedido.RECEBIDO).build();
        
        when(repository.findAllByStatusIn(List.of(StatusPedido.RECEBIDO, StatusPedido.EM_PREPARACAO)))
                .thenReturn(List.of(p1));

        List<PedidoProducao> resultado = service.listarFilaCozinha();

        Assertions.assertFalse(resultado.isEmpty());
        Assertions.assertEquals(StatusPedido.RECEBIDO, resultado.get(0).getStatus());
    }
}