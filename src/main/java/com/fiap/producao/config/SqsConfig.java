package com.fiap.producao.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsConfig {

    // 1. Cria o Cliente SQS com as credenciais do ambiente (K8s)
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    // 2. Define o Conversor customizado como Bean.
    // O Spring Cloud AWS vai detectar este Bean automaticamente e usar no Listener.
    @Bean
    public SqsMessagingMessageConverter sqsMessagingMessageConverter(ObjectMapper objectMapper) {
        SqsMessagingMessageConverter converter = new SqsMessagingMessageConverter();
        converter.setObjectMapper(objectMapper);

        // O PULO DO GATO: Ignora o cabeçalho 'JavaType' que vem do Pedido
        converter.setPayloadTypeHeader(null);

        return converter;
    }
}