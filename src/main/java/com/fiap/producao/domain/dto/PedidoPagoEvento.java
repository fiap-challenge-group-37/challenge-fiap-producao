package com.fiap.producao.domain.dto;

import com.fiap.producao.domain.entity.ItemProducao;
import java.util.List;

public record PedidoPagoEvento(
    Long idPedido,
    List<ItemProducao> itens
) {}