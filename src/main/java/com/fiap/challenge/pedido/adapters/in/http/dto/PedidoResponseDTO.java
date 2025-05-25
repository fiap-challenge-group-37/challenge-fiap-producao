package com.fiap.challenge.pedido.adapters.in.http.dto;

import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoResponseDTO {
    private Long id;
    private Long clienteId;
    private List<ItemPedidoResponseDTO> itens; // Usaremos um DTO específico para itens na resposta
    private BigDecimal valorTotal;
    private String status; // String para facilitar a exibição
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public PedidoResponseDTO(Long id, Long clienteId, List<ItemPedidoResponseDTO> itens, BigDecimal valorTotal, String status, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.itens = itens;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public static PedidoResponseDTO fromDomain(Pedido pedido) {
        if (pedido == null) return null;
        List<ItemPedidoResponseDTO> itemDTOs = pedido.getItens().stream()
                .map(ItemPedidoResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getClienteId(),
                itemDTOs,
                pedido.getValorTotal(),
                pedido.getStatus().getDescricao(), // Usar a descrição do Enum
                pedido.getDataCriacao(),
                pedido.getDataAtualizacao()
        );
    }

    // Getters
    public Long getId() { return id; }
    public Long getClienteId() { return clienteId; }
    public List<ItemPedidoResponseDTO> getItens() { return itens; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public String getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
}