package com.fiap.producao.worker;

import com.fiap.producao.domain.dto.PedidoPagoEvento;
import com.fiap.producao.domain.entity.ItemProducao;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class PedidoPagoListener {

    private final PedidoProducaoRepository repository;

    public PedidoPagoListener(PedidoProducaoRepository repository) {
        this.repository = repository;
    }

    @SqsListener("${events.queue.pedido-pago}")
    public void receber(PedidoPagoEvento evento) {

        var itens = evento.itens().stream()
                .map(i -> new ItemProducao(i.nome(), i.quantidade()))
                .collect(Collectors.toList());

        PedidoProducao pedido = PedidoProducao.builder()
                .id(evento.idPedido().toString())
                .idPedidoOriginal(evento.idPedido())
                .itens(itens)
                .status(StatusPedido.RECEBIDO)
                .dataEntrada(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .build();

        repository.save(pedido);
    }
}
