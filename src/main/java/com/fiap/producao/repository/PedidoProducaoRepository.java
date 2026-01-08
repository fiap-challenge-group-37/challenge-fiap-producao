package com.fiap.producao.repository;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PedidoProducaoRepository {

    private final DynamoDbTable<PedidoProducao> table;

    public PedidoProducaoRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                    @Value("${aws.dynamodb.table-name}") String tableName) {
        this.table = dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(PedidoProducao.class));
    }

    public PedidoProducao save(PedidoProducao pedido) {
        table.putItem(pedido);
        return pedido;
    }

    public Optional<PedidoProducao> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return Optional.ofNullable(table.getItem(key));
    }

    public List<PedidoProducao> findAll() {
        return table.scan().items().stream().collect(Collectors.toList());
    }

    public void deleteAll() {
        findAll().forEach(table::deleteItem);
    }

    public List<PedidoProducao> findByStatusNot(StatusPedido status) {
        AttributeValue statusValue = AttributeValue.builder().s(status.name()).build();

        Expression filterExpression = Expression.builder()
                .expression("#st <> :status")
                .expressionValues(Map.of(":status", statusValue))
                .expressionNames(Map.of("#st", "status"))
                .build();

        return table.scan(ScanEnhancedRequest.builder()
                        .filterExpression(filterExpression)
                        .build())
                .items().stream().collect(Collectors.toList());
    }
}