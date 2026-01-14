package com.fiap.producao.worker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.producao.domain.entity.ItemProducao;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoPagoListener {

    private final PedidoProducaoRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PedidoPagoListener(PedidoProducaoRepository repository) {
        this.repository = repository;
    }

    @SqsListener("${events.queue.pedido-pago}")
    public void receber(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);

            // Extrai campos do JSON
            Long idPedido = node.has("idPedido") ? node.get("idPedido").asLong() : null;
            List<ItemProducao> itens = new ArrayList<>();
            if (node.has("itens") && node.get("itens").isArray()) {
                for (JsonNode itemNode : node.get("itens")) {
                    String nome = itemNode.has("nome") ? itemNode.get("nome").asText() : null;
                    int qtd = itemNode.has("quantidade") ? itemNode.get("quantidade").asInt() : 0;
                    itens.add(new ItemProducao(nome, qtd));
                }
            }

            PedidoProducao pedido = PedidoProducao.builder()
                    .id(idPedido != null ? idPedido.toString() : null)
                    .idPedidoOriginal(idPedido)
                    .itens(itens)
                    .status(StatusPedido.RECEBIDO)
                    .dataEntrada(LocalDateTime.now())
                    .dataAtualizacao(LocalDateTime.now())
                    .build();

            repository.save(pedido);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar mensagem SQS de pedido pago", e);
        }
    }
}