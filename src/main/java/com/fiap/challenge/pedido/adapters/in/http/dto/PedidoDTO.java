package com.fiap.challenge.pedido.adapters.in.http.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class PedidoDTO {

    private Long clienteId; // Opcional, o cliente pode não se identificar [cite: 14]

    @NotEmpty(message = "Lista de itens não pode ser vazia.")
    @Size(min = 1, message = "Pedido deve conter ao menos um item.")
    @Valid // Para validar os ItemPedidoDTO dentro da lista
    private List<ItemPedidoDTO> itens;

    // Getters e Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}