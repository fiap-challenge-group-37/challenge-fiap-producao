package com.fiap.producao.repository;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PedidoProducaoRepository extends MongoRepository<PedidoProducao, String> {
    // Usado pelo código de produção (ex: buscar tudo que NÃO está FINALIZADO)
    List<PedidoProducao> findByStatusNot(StatusPedido status);
    
    // Mantemos este caso algum outro ponto use, ou podemos remover depois
    List<PedidoProducao> findAllByStatusIn(List<StatusPedido> status);
}