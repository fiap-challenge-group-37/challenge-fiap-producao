package com.fiap.producao.bdd;

import com.fiap.producao.domain.dto.PedidoPagoEvento;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import com.fiap.producao.worker.PedidoPagoListener;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class StepDefinition {

    @Autowired
    private PedidoPagoListener pedidoPagoListener;

    @Autowired
    private PedidoProducaoRepository repository;

    private PedidoProducao pedidoResultado;

    @Before
    public void setup() {
        repository.deleteAll();
    }

    @Dado("que um pedido com ID {long} e itens {string} foi pago")
    public void que_um_pedido_foi_pago(Long idPedido, String nomeItem) {
        // ✅ Monte o evento com o DTO correto do evento (não use ItemProducao aqui)
        var itemEvento = new PedidoPagoEvento.ItemEvento(nomeItem, 1);
        var evento = new PedidoPagoEvento(idPedido, List.of(itemEvento));

        // ✅ chame o método real do listener
        pedidoPagoListener.receber(evento);
    }

    @Quando("o worker processar a mensagem de pagamento")
    public void o_worker_processar_a_mensagem() {
        // Síncrono: já processou no passo anterior
    }

    @Entao("o pedido deve estar na fila com status {string}")
    public void o_pedido_deve_estar_na_fila_com_status(String statusEsperado) {
        var pedidos = repository.findAll();
        Assertions.assertFalse(pedidos.isEmpty());
        Assertions.assertEquals(StatusPedido.valueOf(statusEsperado), pedidos.get(0).getStatus());
    }

    @Dado("que existe um pedido na fila com ID {string} e status {string}")
    public void que_existe_um_pedido_na_fila(String id, String status) {
        PedidoProducao pedido = PedidoProducao.builder()
                .id(id)
                .status(StatusPedido.valueOf(status))
                .dataEntrada(LocalDateTime.now())
                .build();

        repository.save(pedido);
    }

    @Quando("eu atualizo o status do pedido {string} para {string}")
    public void eu_atualizo_o_status_do_pedido(String id, String novoStatus) {
        var pedidoOptional = repository.findById(id);
        if (pedidoOptional.isPresent()) {
            PedidoProducao pedido = pedidoOptional.get();
            pedido.setStatus(StatusPedido.valueOf(novoStatus));
            pedidoResultado = repository.save(pedido);
        }
    }

    @Entao("o pedido deve aparecer na lista com status {string}")
    public void o_pedido_deve_aparecer_na_lista_com_status(String statusEsperado) {
        Assertions.assertNotNull(pedidoResultado);
        Assertions.assertEquals(StatusPedido.valueOf(statusEsperado), pedidoResultado.getStatus());
    }
}
