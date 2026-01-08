package com.fiap.producao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = {
        "events.queue.pedido-pago=fila-teste-mock",
        "spring.cloud.aws.sqs.enabled=false"
})
class ProducaoApplicationTests {

    @Autowired
    private ApplicationContext context;

    // Substitui o cliente real por um Mock durante este teste
    @MockBean
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Test
    void contextLoads() {
        when(dynamoDbEnhancedClient.table(anyString(), any(TableSchema.class)))
                .thenReturn(mock(DynamoDbTable.class));

        Assertions.assertNotNull(context, "O contexto da aplicacao nao deve ser nulo");
    }
}