package com.fiap.challenge.pedido.domain.entities;

import java.math.BigDecimal;

public class ItemPedido {
    private Long id;
    private Long produtoId; // Referência ao ID do produto
    private String nomeProduto; // Pode ser útil para exibição sem consultar o produto
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;

    // Construtor para criação
    public ItemPedido(Long produtoId, String nomeProduto, Integer quantidade, BigDecimal precoUnitario) {
        if (produtoId == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo.");
        }
        if (nomeProduto == null || nomeProduto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto não pode ser vazio.");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva.");
        }
        if (precoUnitario == null || precoUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço unitário não pode ser nulo ou negativo.");
        }
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    // Construtor para reconstrução (ex: do banco de dados)
    public ItemPedido(Long id, Long produtoId, String nomeProduto, Integer quantidade, BigDecimal precoUnitario, BigDecimal precoTotal) {
        this(produtoId, nomeProduto, quantidade, precoUnitario);
        this.id = id;
        // Recalcula para garantir consistência, ou confia no valor do BD
        this.precoTotal = (precoTotal != null) ? precoTotal : this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
    }


    // Getters
    public Long getId() {
        return id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    // Setters (com validações se necessário)
    public void setId(Long id) {
        this.id = id;
    }
}