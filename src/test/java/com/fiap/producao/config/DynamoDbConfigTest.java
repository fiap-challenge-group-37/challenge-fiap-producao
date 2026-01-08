package com.fiap.producao.config;

import com.fiap.producao.domain.entity.PedidoProducao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.CommandLineRunner;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DynamoDbConfigTest {

    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Mock
    private DynamoDbTable<PedidoProducao> dynamoDbTable;

    @Test
    void deveCriarTabelaSeNaoExistir() throws Exception {
        // Arrange
        String tableName = "tabela-teste";
        DynamoDbConfig config = new DynamoDbConfig(); // Se der erro aqui, verifique se a classe DynamoDbConfig existe na pasta config/

        when(dynamoDbEnhancedClient.table(eq(tableName), any(TableSchema.class)))
                .thenReturn(dynamoDbTable);

        // Act
        CommandLineRunner runner = config.criarTabelaSeNaoExistir(dynamoDbEnhancedClient, tableName);
        runner.run();

        // Assert
        verify(dynamoDbTable, times(1)).createTable();
    }

    @Test
    void deveIgnorarErroSeTabelaJaExistir() throws Exception {
        // Arrange
        String tableName = "tabela-teste";
        DynamoDbConfig config = new DynamoDbConfig();

        when(dynamoDbEnhancedClient.table(eq(tableName), any(TableSchema.class)))
                .thenReturn(dynamoDbTable);

        doThrow(new RuntimeException("Tabela já existe")).when(dynamoDbTable).createTable();

        // Act
        CommandLineRunner runner = config.criarTabelaSeNaoExistir(dynamoDbEnhancedClient, tableName);
        runner.run();

        // Assert
        verify(dynamoDbTable, times(1)).createTable();
    }
}