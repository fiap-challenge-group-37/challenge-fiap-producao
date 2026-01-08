package com.fiap.producao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@SpringBootTest(properties = {
        "events.queue.pedido-pago=fila-teste-mock",
        "spring.cloud.aws.sqs.enabled=false"
})
class ProducaoApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(context, "O contexto da aplicacao nao deve ser nulo");
    }

    // Configuração extra apenas para este teste subir o contexto sem erros
    @TestConfiguration
    static class TestConfig {
        @Bean
        public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
            DynamoDbEnhancedClient client = Mockito.mock(DynamoDbEnhancedClient.class);
            Mockito.when(client.table(Mockito.anyString(), Mockito.any(TableSchema.class)))
                    .thenReturn(Mockito.mock(DynamoDbTable.class));
            return client;
        }
    }
}