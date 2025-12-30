package com.fiap.challenge.pagamento.adapters.out;

import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.MercadoPagoQrCodeResponse;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.MercadoPagoPagamentoResponse;
import com.fiap.challenge.pagamento.adapters.out.mercadopago.dto.PagamentoDTO;
import com.fiap.challenge.pedido.domain.entities.ItemPedido;
import com.fiap.challenge.pedido.domain.entities.Pedido;
import com.fiap.challenge.pedido.domain.entities.StatusPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MercadoPagoClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MercadoPagoProperties properties;

    @InjectMocks
    private MercadoPagoClient mercadoPagoClient;

    private Pedido pedidoTeste;

    @BeforeEach
    void setUp() {
        ItemPedido item = new ItemPedido(1L, "Hamburguer", 2, BigDecimal.valueOf(25.50));
        pedidoTeste = new Pedido(1L, 10L, Arrays.asList(item), BigDecimal.valueOf(51.00), 
                                StatusPedido.AGUARDANDO_PAGAMENTO, LocalDateTime.now(), LocalDateTime.now(), 
                                "EXT-12345", "qr-code-test");
    }

    @Test
    @DisplayName("Deve construir MercadoPagoClient corretamente")
    void deveConstruirMercadoPagoClientCorretamente() {
        MercadoPagoClient client = new MercadoPagoClient(restTemplate, properties);
        
        assertNotNull(client);
    }

    @Test
    @DisplayName("Deve criar pagamento com sucesso")
    void deveCriarPagamentoComSucesso() {
        // Arrange - Mock das propriedades
        when(properties.getUrl()).thenReturn("https://api.mercadopago.com");
        when(properties.getUserId()).thenReturn("123456");
        when(properties.getExternalPosId()).thenReturn("POS01");
        when(properties.getWebhookPagamento()).thenReturn("https://webhook.test.com");
        when(properties.getAccessToken()).thenReturn("TEST-ACCESS-TOKEN");
        
        MercadoPagoQrCodeResponse expectedResponse = new MercadoPagoQrCodeResponse();
        expectedResponse.setQrData("qr-code-response-test");

        ResponseEntity<MercadoPagoQrCodeResponse> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(MercadoPagoQrCodeResponse.class)))
                .thenReturn(responseEntity);

        // Act
        MercadoPagoQrCodeResponse result = mercadoPagoClient.criarPagamento(pedidoTeste);

        // Assert
        assertNotNull(result);
        assertEquals("qr-code-response-test", result.getQrData());
        
        ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).postForEntity(anyString(), httpEntityCaptor.capture(), eq(MercadoPagoQrCodeResponse.class));
        
        HttpEntity<?> capturedEntity = httpEntityCaptor.getValue();
        assertNotNull(capturedEntity.getHeaders());
        assertTrue(capturedEntity.getHeaders().containsKey("Authorization"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando falha ao criar pagamento")
    void deveLancarExcecaoQuandoFalhaAoCriarPagamento() {
        // Arrange - Mock das propriedades apenas necessárias
        when(properties.getUrl()).thenReturn("https://api.mercadopago.com");
        when(properties.getAccessToken()).thenReturn("TEST-ACCESS-TOKEN");
        when(properties.getUserId()).thenReturn("123456");
        when(properties.getExternalPosId()).thenReturn("POS01");
        when(properties.getWebhookPagamento()).thenReturn("https://webhook.test.com");
        
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(MercadoPagoQrCodeResponse.class)))
                .thenThrow(new RestClientException("Erro na API"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            mercadoPagoClient.criarPagamento(pedidoTeste);
        });
    }

    @Test
    @DisplayName("Deve buscar pagamento por external reference com sucesso")
    void deveBuscarPagamentoPorExternalReferenceComSucesso() {
        // Arrange - Mock das propriedades
        when(properties.getUrl()).thenReturn("https://api.mercadopago.com");
        when(properties.getAccessToken()).thenReturn("TEST-ACCESS-TOKEN");
        
        String externalReference = "EXT-12345";
        MercadoPagoPagamentoResponse expectedResponse = new MercadoPagoPagamentoResponse();
        // Configure o response conforme necessário

        ResponseEntity<MercadoPagoPagamentoResponse> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(MercadoPagoPagamentoResponse.class)))
                .thenReturn(responseEntity);

        // Act
        MercadoPagoPagamentoResponse result = mercadoPagoClient.getPaymentByExternalReference(externalReference);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    @DisplayName("Deve buscar pagamento por ID com sucesso")
    void deveBuscarPagamentoPorIdComSucesso() {
        // Arrange - Mock das propriedades
        when(properties.getUrl()).thenReturn("https://api.mercadopago.com");
        when(properties.getAccessToken()).thenReturn("TEST-ACCESS-TOKEN");
        
        String paymentId = "123456789";
        PagamentoDTO expectedResponse = new PagamentoDTO();
        // Configure o DTO conforme necessário

        ResponseEntity<PagamentoDTO> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(PagamentoDTO.class)))
                .thenReturn(responseEntity);

        // Act
        PagamentoDTO result = mercadoPagoClient.getPaymentById(paymentId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);
    }

    @Test
    @DisplayName("Deve lançar exceção quando falha ao buscar pagamento por external reference")
    void deveLancarExcecaoQuandoFalhaAoBuscarPagamentoPorExternalReference() {
        // Arrange - Mock das propriedades
        when(properties.getUrl()).thenReturn("https://api.mercadopago.com");
        when(properties.getAccessToken()).thenReturn("TEST-ACCESS-TOKEN");
        
        String externalReference = "EXT-12345";
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(MercadoPagoPagamentoResponse.class)))
                .thenThrow(new RestClientException("Erro na API"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            mercadoPagoClient.getPaymentByExternalReference(externalReference);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção quando falha ao buscar pagamento por ID")
    void deveLancarExcecaoQuandoFalhaAoBuscarPagamentoPorId() {
        // Arrange - Mock das propriedades
        when(properties.getUrl()).thenReturn("https://api.mercadopago.com");
        when(properties.getAccessToken()).thenReturn("TEST-ACCESS-TOKEN");
        
        String paymentId = "123456789";
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(PagamentoDTO.class)))
                .thenThrow(new RestClientException("Erro na API"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            mercadoPagoClient.getPaymentById(paymentId);
        });
    }
}