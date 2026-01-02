package com.fiap.producao.worker;

import com.fiap.producao.domain.dto.PedidoPagoEvento;
import com.fiap.producao.domain.entity.ItemProducao;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PedidoPagoListenerTest {

    @InjectMocks
    private PedidoPagoListener listener;

    @Mock
    private PedidoProducaoRepository repository;

    @Test
    void deveSalvarPedidoQuandoReceberMensagem() {
        ItemProducao item = new ItemProducao("Hamburguer", 2);
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, List.of(item));

        listener.receberMensagem(evento);

        verify(repository).save(any());
    }
}