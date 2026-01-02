package com.fiap.producao.worker;

import com.fiap.producao.domain.dto.PedidoPagoEvento;
import com.fiap.producao.domain.entity.ItemProducao;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoPagoListener {

    private final PedidoProducaoRepository repository;

    @SqsListener("${events.queue.pedido-pago}")
    public void receberMensagem(PedidoPagoEvento evento) {
        // Correcao: record usa o nome do campo direto (idPedido())
        log.info("Recebi um pedido pago! ID: {}", evento.idPedido());

        PedidoProducao pedido = new PedidoProducao();
        pedido.setId(String.valueOf(evento.idPedido())); 
        pedido.setStatus(StatusPedido.RECEBIDO);
        pedido.setDataEntrada(LocalDateTime.now());

        // Correcao: record usa itens() sem o get
        if (evento.itens() != null) {
            List<ItemProducao> itens = evento.itens().stream()
                    // ItemProducao parece ser uma classe normal (Entity), entao mantemos o getNome()
                    .map(item -> new ItemProducao(item.getNome(), item.getQuantidade()))
                    .collect(Collectors.toList());
            pedido.setItens(itens);
        }

        repository.save(pedido);
        log.info("Pedido salvo na producao com sucesso: {}", pedido.getId());
    }
}
