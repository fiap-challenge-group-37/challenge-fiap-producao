package com.fiap.producao.worker;

import com.fiap.producao.domain.dto.PedidoPagoEvento;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import java.time.LocalDateTime;

@Component
// Esta linha faz a mágica: só cria o bean se app.sqs.enabled=true
@ConditionalOnProperty(name = "app.sqs.enabled", havingValue = "true", matchIfMissing = false)
public class PedidoPagoListener {

    private final PedidoProducaoRepository repository;

    public PedidoPagoListener(PedidoProducaoRepository repository) {
        this.repository = repository;
    }

    @SqsListener("pedidos-pagos-queue")
    public void receberMensagem(PedidoPagoEvento evento) {
        System.out.println("Recebi um pedido pago! ID: " + evento.idPedido());

        PedidoProducao novoPedido = PedidoProducao.builder()
                .idPedidoOriginal(evento.idPedido())
                .itens(evento.itens())
                .status(StatusPedido.RECEBIDO)
                .dataEntrada(LocalDateTime.now())
                .build();

        repository.save(novoPedido);
    }
}