package com.fiap.challenge.pedido.adapters.out.persistence;

import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import com.fiap.challenge.pedido.domain.port.PedidoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PedidoRepositoryDatabase implements PedidoRepository {

    private final PedidoJpaRepository jpaRepository;

    public PedidoRepositoryDatabase(PedidoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {
        PedidoEntity entity = PedidoEntity.fromDomain(pedido);
        // Garante que a relação bidirecional está correta antes de salvar
        if (entity.getItens() != null) {
            entity.getItens().forEach(itemEntity -> itemEntity.setPedido(entity));
        }
        PedidoEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> findById(Long id) {
        return jpaRepository.findById(id).map(PedidoEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findAll() {
        // Retorna a lista ordenada para acompanhamento
        return jpaRepository.findPedidosNaoFinalizadosOrdenadosParaCozinha().stream()
                .map(PedidoEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> findByStatus(StatusPedido status) {
        return jpaRepository.findByStatus(status).stream()
                .map(PedidoEntity::toDomain)
                .collect(Collectors.toList());
    }
}