package com.fiap.producao.repository;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoProducaoRepositoryTest {

    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Mock
    private DynamoDbTable<PedidoProducao> dynamoDbTable;

    @Mock
    private PageIterable<PedidoProducao> pageIterable;

    @Mock
    private SdkIterable<PedidoProducao> sdkIterable; // Mock explícito do Iterable do SDK

    private PedidoProducaoRepository repository;

    @BeforeEach
    void setUp() {
        // Mock genérico da tabela para todos os testes
        lenient().when(dynamoDbEnhancedClient.table(anyString(), any(TableSchema.class)))
                .thenReturn(dynamoDbTable);

        repository = new PedidoProducaoRepository(dynamoDbEnhancedClient, "tabela-teste");
    }

    @Test
    void deveSalvarPedido() {
        PedidoProducao pedido = new PedidoProducao();
        pedido.setId("123");

        PedidoProducao salvo = repository.save(pedido);

        assertNotNull(salvo);
        verify(dynamoDbTable, times(1)).putItem(pedido);
    }

    @Test
    void deveBuscarPorId() {
        PedidoProducao pedido = new PedidoProducao();
        pedido.setId("123");

        when(dynamoDbTable.getItem(any(Key.class))).thenReturn(pedido);

        Optional<PedidoProducao> resultado = repository.findById("123");

        assertTrue(resultado.isPresent());
        assertEquals("123", resultado.get().getId());
    }

    @Test
    void deveListarTodos() {
        // Arrange
        List<PedidoProducao> dados = List.of(new PedidoProducao(), new PedidoProducao());

        // Configura a cadeia de chamadas: scan() -> items() -> stream()
        when(dynamoDbTable.scan()).thenReturn(pageIterable);
        when(pageIterable.items()).thenReturn(sdkIterable);
        when(sdkIterable.stream()).thenReturn(dados.stream());

        // Act
        List<PedidoProducao> lista = repository.findAll();

        // Assert
        assertEquals(2, lista.size());
    }

    @Test
    void deveDeletarTodos() {
        // Arrange - Precisa simular o findAll() antes de deletar
        List<PedidoProducao> dados = List.of(new PedidoProducao());

        when(dynamoDbTable.scan()).thenReturn(pageIterable);
        when(pageIterable.items()).thenReturn(sdkIterable);
        when(sdkIterable.stream()).thenReturn(dados.stream());

        // Act
        repository.deleteAll();

        // Assert
        verify(dynamoDbTable, times(1)).deleteItem(any(PedidoProducao.class));
    }

    @Test
    void deveBuscarPorStatusNot() {
        // Arrange
        List<PedidoProducao> dados = List.of(new PedidoProducao());

        // Nota: O Repository usa table.scan(Request), não table.scan() vazio
        when(dynamoDbTable.scan(any(ScanEnhancedRequest.class))).thenReturn(pageIterable);
        when(pageIterable.items()).thenReturn(sdkIterable);
        when(sdkIterable.stream()).thenReturn(dados.stream());

        // Act
        List<PedidoProducao> lista = repository.findByStatusNot(StatusPedido.FINALIZADO);

        // Assert
        assertFalse(lista.isEmpty());
        verify(dynamoDbTable).scan(any(ScanEnhancedRequest.class));
    }
}