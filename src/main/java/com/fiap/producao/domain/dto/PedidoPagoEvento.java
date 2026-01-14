package com.fiap.producao.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PedidoPagoEvento(
        @JsonProperty("idPedido") // Mapeia o campo "idPedido" do JSON
        Long idPedido,

        @JsonProperty("itens")    // Mapeia o campo "itens" do JSON
        List<ItemEvento> itens
) {
    public record ItemEvento(
            @JsonProperty("nome")       // Mapeia "nome"
            String nome,

            @JsonProperty("quantidade") // Mapeia "quantidade"
            Integer quantidade
    ) {}
}