package com.fiap.challenge.pedido.adapters.out.persistence;

import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import com.fiap.challenge.pedido.domain.entities.ItemPedido; // Import para o mapeamento
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long clienteId; // Pode ser nulo

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ItemPedidoEntity> itens = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusPedido status;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    public Pedido toDomain() {
        List<ItemPedido> domainItens = this.itens.stream()
                .map(ItemPedidoEntity::toDomain)
                .collect(Collectors.toList());
        return new Pedido(this.id, this.clienteId, domainItens, this.valorTotal, this.status, this.dataCriacao, this.dataAtualizacao);
    }

    public static PedidoEntity fromDomain(Pedido pedido) {
        PedidoEntity entity = new PedidoEntity();
        entity.setId(pedido.getId()); // Pode ser nulo na criação
        entity.setClienteId(pedido.getClienteId());
        entity.setValorTotal(pedido.getValorTotal());
        entity.setStatus(pedido.getStatus());
        entity.setDataCriacao(pedido.getDataCriacao() == null ? LocalDateTime.now() : pedido.getDataCriacao());
        entity.setDataAtualizacao(pedido.getDataAtualizacao() == null ? LocalDateTime.now() : pedido.getDataAtualizacao());

        // Mapear itens
        if (pedido.getItens() != null) {
            List<ItemPedidoEntity> itemEntities = pedido.getItens().stream()
                    .map(itemDomain -> ItemPedidoEntity.fromDomain(itemDomain, entity))
                    .collect(Collectors.toList());
            entity.setItens(itemEntities);
        }
        return entity;
    }
}