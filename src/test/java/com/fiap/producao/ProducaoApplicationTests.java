package com.fiap.producao;

import com.fiap.producao.integration.PedidoIntegrationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.jwt.JwtDecoder;
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

    @MockBean
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @MockBean
    private PedidoIntegrationService pedidoIntegrationService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void contextLoads() {
        // Mock do comportamento do DynamoDB para não quebrar a inicialização do Bean de configuração
        when(dynamoDbEnhancedClient.table(anyString(), any(TableSchema.class)))
                .thenReturn(mock(DynamoDbTable.class));

        Assertions.assertNotNull(context, "O contexto da aplicacao nao deve ser nulo");
    }
}