package com.fiap.producao.service;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoProducaoRepository repository;

    public PedidoService(PedidoProducaoRepository repository) {
        this.repository = repository;
    }

    public List<PedidoProducao> listarFilaCozinha() {
        List<PedidoProducao> pedidos = repository.findByStatusNot(StatusPedido.FINALIZADO);

        return pedidos.stream()
                .sorted(Comparator.comparing(this::getPrioridade)
                        .thenComparing(PedidoProducao::getDataEntrada))
                .collect(Collectors.toList());
    }

    int getPrioridade(PedidoProducao p) {
        return switch (p.getStatus()) {
            case PRONTO -> 1;
            case EM_PREPARACAO -> 2;
            case RECEBIDO -> 3;
            default -> 99;
        };
    }
}
