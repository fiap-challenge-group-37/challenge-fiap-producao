package com.fiap.producao.domain.dto;

import java.util.List;

public record PedidoPagoEvento(
        Long idPedido,
        List<ItemEvento> itens
) {
    public record ItemEvento(
            String nome,
            Integer quantidade
    ) {}
}
