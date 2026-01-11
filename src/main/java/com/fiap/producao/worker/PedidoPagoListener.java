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
        log.info("Recebi um pedido pago! ID: {}", evento.idPedido());

        // Validação de segurança para o ID
        if (evento.idPedido() == null) {
            log.error("Pedido recebido sem ID! Ignorando mensagem.");
            return;
        }

        PedidoProducao pedido = new PedidoProducao();
        pedido.setId(String.valueOf(evento.idPedido()));
        pedido.setIdPedidoOriginal(evento.idPedido()); // Importante salvar o ID original num campo numérico se precisar depois
        pedido.setStatus(StatusPedido.RECEBIDO);
        pedido.setDataEntrada(LocalDateTime.now());

        if (evento.itens() != null) {
            List<ItemProducao> itens = evento.itens().stream()
                    .map(item -> {
                        // O DynamoDB REJEITA Strings vazias ("") com erro 400.
                        // Garantimos que sempre haja um texto válido.
                        String nomeItem = (item.getNome() == null || item.getNome().trim().isEmpty())
                                ? "Item sem nome"
                                : item.getNome();

                        return new ItemProducao(nomeItem, item.getQuantidade());
                    })
                    .collect(Collectors.toList());
            pedido.setItens(itens);
        }

        try {
            repository.save(pedido);
            log.info("Pedido salvo na producao com sucesso: {}", pedido.getId());
        } catch (Exception e) {
            log.error("Erro ao salvar no DynamoDB: {}", e.getMessage(), e);
            throw e; // Relança para a mensagem voltar para a DLQ se necessário
        }
    }
}