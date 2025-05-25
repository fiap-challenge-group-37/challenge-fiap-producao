package com.fiap.challenge.pedido.domain.port;

import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
    Pedido save(Pedido pedido);
    Optional<Pedido> findById(Long id);
    List<Pedido> findAll();
    List<Pedido> findByStatus(StatusPedido status);
    // Poderia ter um método para listar pedidos por ordem de chegada para a cozinha:
    // List<Pedido> findByStatusOrderByDataCriacaoAsc(List<StatusPedido> status);
}