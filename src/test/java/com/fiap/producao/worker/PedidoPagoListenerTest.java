package com.fiap.producao.worker;

import com.fiap.producao.domain.dto.PedidoPagoEvento;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PedidoPagoListenerTest {

    @Test
    void deve_salvar_pedido_com_status_recebido_e_idPedidoOriginal() {
        // arrange
        PedidoProducaoRepository repository = mock(PedidoProducaoRepository.class);
        PedidoPagoListener listener = new PedidoPagoListener(repository);

        var item = new PedidoPagoEvento.ItemEvento("Hambúrguer", 10);
        var evento = new PedidoPagoEvento(13L, List.of(item));

        // act
        listener.receber(evento);

        // assert
        ArgumentCaptor<PedidoProducao> captor = ArgumentCaptor.forClass(PedidoProducao.class);
        verify(repository, times(1)).save(captor.capture());

        PedidoProducao salvo = captor.getValue();

        assertThat(salvo.getId()).isEqualTo("13");
        assertThat(salvo.getIdPedidoOriginal()).isEqualTo(13L);
        assertThat(salvo.getStatus()).isEqualTo(StatusPedido.RECEBIDO);

        assertThat(salvo.getItens()).hasSize(1);
        assertThat(salvo.getItens().get(0).getNome()).isEqualTo("Hambúrguer");
        assertThat(salvo.getItens().get(0).getQuantidade()).isEqualTo(10);

        assertThat(salvo.getDataEntrada()).isNotNull();
        assertThat(salvo.getDataAtualizacao()).isNotNull();
    }
}
