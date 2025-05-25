package com.fiap.challenge.pedido.adapters.in.http.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ItemPedidoDTO {

    @NotNull(message = "ID do produto não pode ser nulo.")
    private Long produtoId;

    // O nome do produto e o preço unitário seriam buscados no momento da criação do pedido,
    // com base no produtoId, para evitar que o cliente envie esses dados e haja inconsistência.
    // No entanto, para o "fake checkout" onde apenas enviamos os produtos escolhidos para a fila[cite: 30],
    // podemos precisar apenas do ID e da quantidade.
    // Vamos assumir que o backend buscará os detalhes do produto.

    @NotNull(message = "Quantidade não pode ser nula.")
    @Positive(message = "Quantidade deve ser positiva.")
    private Integer quantidade;

    // Getters e Setters
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}