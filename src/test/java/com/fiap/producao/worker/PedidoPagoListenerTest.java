package com.fiap.producao.worker;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PedidoPagoListenerTest {

    @Test
    void deve_salvar_pedido_com_status_recebido_e_idPedidoOriginal() {
        // arrange
        PedidoProducaoRepository repository = mock(PedidoProducaoRepository.class);
        PedidoPagoListener listener = new PedidoPagoListener(repository);

        // CORREÇÃO: Simulamos o JSON que vem da fila
        String json = """
            {
              "idPedido": 13,
              "itens": [
                {
                  "nome": "Hambúrguer",
                  "quantidade": 10
                }
              ]
            }
            """;

        // act
        listener.receber(json);

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