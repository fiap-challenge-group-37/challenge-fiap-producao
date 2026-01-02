package com.fiap.producao.repository;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoProducaoRepository extends MongoRepository<PedidoProducao, String> {
    List<PedidoProducao> findByStatusNot(StatusPedido status);
}
