package com.fiap.producao.config;

import com.fiap.producao.domain.entity.PedidoProducao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class DynamoDbConfig {

    @Bean
    @ConditionalOnBean(DynamoDbEnhancedClient.class) // Só roda se o DynamoDB estiver ativo
    public CommandLineRunner criarTabelaSeNaoExistir(DynamoDbEnhancedClient client,
                                                     @Value("${aws.dynamodb.table-name}") String tableName) {
        return args -> {
            try {
                DynamoDbTable<PedidoProducao> table = client.table(tableName, TableSchema.fromBean(PedidoProducao.class));
                table.createTable();
                System.out.println("Tabela " + tableName + " criada/verificada com sucesso.");
            } catch (Exception e) {
                System.out.println("Status da tabela: " + e.getMessage());
            }
        };
    }
}