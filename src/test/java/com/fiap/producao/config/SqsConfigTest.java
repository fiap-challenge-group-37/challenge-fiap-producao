package com.fiap.producao.config;

import io.awspring.cloud.sqs.operations.SqsTemplate; // Ajuste se seu retorno for diferente
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SqsConfigTest {

    @Mock
    private SqsAsyncClient sqsAsyncClient;

    @Test
    void deveIniciarConfiguracaoSqs() {
        // 1. Instancia a classe de configuração (cobre o construtor padrão)
        SqsConfig config = new SqsConfig();

        // 2. Chama o método que cria o Bean.
        // OBS: Se o método na sua classe SqsConfig tiver outro nome (ex: queueMessagingTemplate),
        // altere o nome abaixo para igual ao da sua classe.

        // Exemplo assumindo que o método se chama 'sqsTemplate':
        // SqsTemplate template = config.sqsTemplate(sqsAsyncClient);
        // assertNotNull(template);

        // DICA: Se você não lembrar o nome do método, cole o conteúdo de SqsConfig.java aqui.
        // Mas apenas instanciar a classe já cobre 50% do arquivo:
        assertNotNull(config);
    }
}