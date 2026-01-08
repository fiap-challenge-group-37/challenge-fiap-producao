package com.fiap.producao;

import com.fiap.producao.domain.entity.PedidoProducao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@SpringBootApplication
public class ProducaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducaoApplication.class, args);
    }

    @Bean
    public CommandLineRunner criarTabelaSeNaoExistir(DynamoDbEnhancedClient client,
                                                     @Value("${aws.dynamodb.table-name}") String tableName) {
        return args -> {
            try {
                // Mapeia a tabela baseada na entidade
                DynamoDbTable<PedidoProducao> table = client.table(tableName, TableSchema.fromBean(PedidoProducao.class));

                // Tenta criar a tabela no banco (útil para DynamoDB Local)
                table.createTable();
                System.out.println("Tabela " + tableName + " criada/verificada com sucesso.");
            } catch (Exception e) {
                // Se a tabela já existir ou houver outro erro (ex: ambiente produtivo sem permissão de create), apenas loga e segue.
                System.out.println("Inicialização da tabela: " + e.getMessage());
            }
        };
    }
}