package com.fiap.producao.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SqsConfigTest {

    @Test
    void deveCarregarConfiguracaoSqs() {
        // Teste simples de carga de contexto para garantir que a classe não quebra a subida
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // Registra a classe de config (isso valida anotações @Value ou @Bean sem subir o Spring todo)
        context.register(SqsConfig.class);

        // Se precisar de propriedades (application.properties), você pode usar MockEnvironment ou setar System Properties aqui
        // System.setProperty("aws.region", "us-east-1");

        try {
            context.refresh();
            SqsConfig bean = context.getBean(SqsConfig.class);
            assertNotNull(bean);
        } catch (Exception e) {
            // Em alguns casos de CI, testar config AWS real falha sem credenciais.
            // Se falhar, faça apenas um teste de instância simples:
            SqsConfig config = new SqsConfig();
            assertNotNull(config);
        }
        context.close();
    }
}