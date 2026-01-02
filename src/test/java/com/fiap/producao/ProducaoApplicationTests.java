package com.fiap.producao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

// CORRECAO: "spring.cloud.aws.sqs.enabled=false" desliga o SQS totalmente para este teste.
// Assim ele nao tenta conectar, nao tenta resolver fila e nao precisa de Mock.
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
}
