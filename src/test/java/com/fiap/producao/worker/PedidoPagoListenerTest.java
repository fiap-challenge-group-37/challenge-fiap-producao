package com.fiap.producao.worker;

import com.fiap.producao.domain.dto.PedidoPagoEvento;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        properties = {
                "integration.pedidos.url=http://localhost:9999"
        }
)
class PedidoPagoListenerTest {

    // 👉 ISSO resolve o erro do JwtDecoder
    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private PedidoPagoListener listener;

    @Autowired
    private PedidoProducaoRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void deve_salvar_pedido_com_status_recebido_e_idPedidoOriginal() {
        var item = new PedidoPagoEvento.ItemEvento("Hambúrguer", 10);
        var evento = new PedidoPagoEvento(13L, List.of(item));

        listener.receber(evento);

        var pedidos = repository.findAll();
        assertThat(pedidos).hasSize(1);

        PedidoProducao salvo = pedidos.get(0);

        assertThat(salvo.getId()).isEqualTo("13");
        assertThat(salvo.getIdPedidoOriginal()).isEqualTo(13L);
        assertThat(salvo.getStatus()).isEqualTo(StatusPedido.RECEBIDO);
        assertThat(salvo.getItens()).hasSize(1);
    }
}
