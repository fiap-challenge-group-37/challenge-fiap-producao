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

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public SqsMessagingMessageConverter sqsMessagingMessageConverter(ObjectMapper objectMapper) {
        SqsMessagingMessageConverter converter = new SqsMessagingMessageConverter();
        converter.setObjectMapper(objectMapper);

        // CORREÇÃO: Define um mapeador que sempre retorna null.
        // Isso diz ao Spring: "Não tente descobrir a classe olhando a mensagem.
        // Use a classe que está definida no método @SqsListener".
        converter.setPayloadTypeMapper(message -> null);

        return converter;
    }
}