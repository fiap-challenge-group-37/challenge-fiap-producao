package com.fiap.producao.worker;

import com.fiap.producao.domain.dto.PedidoPagoEvento;
import com.fiap.producao.domain.entity.ItemProducao;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoPagoListenerTest {

    @InjectMocks
    private PedidoPagoListener listener;

    @Mock
    private PedidoProducaoRepository repository;

    @Test
    void deveSalvarPedidoQuandoReceberMensagemValida() {
        ItemProducao item = new ItemProducao("Hamburguer", 2);
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, List.of(item));

        listener.receberMensagem(evento);

        // Verifica se salvou e captura o objeto para asserções
        ArgumentCaptor<PedidoProducao> captor = ArgumentCaptor.forClass(PedidoProducao.class);
        verify(repository, times(1)).save(captor.capture());

        PedidoProducao salvo = captor.getValue();
        assertEquals("123", salvo.getId());
        assertEquals("Hamburguer", salvo.getItens().get(0).getNome());
    }

    @Test
    void naoDeveSalvarQuandoIdPedidoForNulo() {
        // Cenário: Mensagem vem sem ID (o código deve ignorar)
        PedidoPagoEvento evento = new PedidoPagoEvento(null, List.of());

        listener.receberMensagem(evento);

        // Garante que o repository.save NUNCA foi chamado
        verify(repository, never()).save(any());
    }

    @Test
    void deveTratarItemComNomeVazioOuNulo() {
        // Cenário: Itens com nomes inválidos que quebrariam o DynamoDB (Erro 400)
        ItemProducao itemVazio = new ItemProducao("", 1);
        ItemProducao itemNulo = new ItemProducao(null, 2);
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, List.of(itemVazio, itemNulo));

        listener.receberMensagem(evento);

        ArgumentCaptor<PedidoProducao> captor = ArgumentCaptor.forClass(PedidoProducao.class);
        verify(repository).save(captor.capture());

        List<ItemProducao> itensSalvos = captor.getValue().getItens();

        // Valida se o código aplicou o valor default "Item sem nome"
        assertEquals("Item sem nome", itensSalvos.get(0).getNome());
        assertEquals("Item sem nome", itensSalvos.get(1).getNome());
    }

    @Test
    void deveProcessarPedidoSemItens() {
        // Cenário: Pedido pago mas sem lista de itens (lista null)
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, null);

        listener.receberMensagem(evento);

        verify(repository).save(any());
    }

    @Test
    void deveRelancarExcecaoQuandoFalharNoBanco() {
        // Cenário: DynamoDB cai ou dá erro. O listener deve relançar para a DLQ funcionar.
        PedidoPagoEvento evento = new PedidoPagoEvento(123L, List.of());
        doThrow(new RuntimeException("Erro de Conexão")).when(repository).save(any());

        assertThrows(RuntimeException.class, () -> listener.receberMensagem(evento));
    }
}