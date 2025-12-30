package com.fiap.challenge.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CalculadoraService - Testes")
class CalculadoraServiceTest {

    private final CalculadoraService calculadoraService = new CalculadoraService();

    @Test
    @DisplayName("Deve calcular desconto corretamente")
    void deveCalcularDescontoCorretamente() {
        // Arrange
        BigDecimal valorTotal = new BigDecimal("100.00");
        BigDecimal percentualDesconto = new BigDecimal("10.00");

        // Act
        BigDecimal resultado = calculadoraService.calcularDesconto(valorTotal, percentualDesconto);

        // Assert
        assertEquals(new BigDecimal("10.00"), resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor total é nulo")
    void deveLancarExcecaoQuandoValorTotalNulo() {
        // Arrange
        BigDecimal percentualDesconto = new BigDecimal("10.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadoraService.calcularDesconto(null, percentualDesconto);
        });

        assertEquals("Valor total e percentual de desconto não podem ser nulos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando percentual é nulo")
    void deveLancarExcecaoQuandoPercentualNulo() {
        // Arrange
        BigDecimal valorTotal = new BigDecimal("100.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadoraService.calcularDesconto(valorTotal, null);
        });

        assertEquals("Valor total e percentual de desconto não podem ser nulos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor total é negativo")
    void deveLancarExcecaoQuandoValorTotalNegativo() {
        // Arrange
        BigDecimal valorTotal = new BigDecimal("-100.00");
        BigDecimal percentualDesconto = new BigDecimal("10.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadoraService.calcularDesconto(valorTotal, percentualDesconto);
        });

        assertEquals("Valor total não pode ser negativo", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando percentual é negativo")
    void deveLancarExcecaoQuandoPercentualNegativo() {
        // Arrange
        BigDecimal valorTotal = new BigDecimal("100.00");
        BigDecimal percentualDesconto = new BigDecimal("-5.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadoraService.calcularDesconto(valorTotal, percentualDesconto);
        });

        assertEquals("Percentual de desconto deve estar entre 0 e 100", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando percentual é maior que 100")
    void deveLancarExcecaoQuandoPercentualMaiorQue100() {
        // Arrange
        BigDecimal valorTotal = new BigDecimal("100.00");
        BigDecimal percentualDesconto = new BigDecimal("150.00");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadoraService.calcularDesconto(valorTotal, percentualDesconto);
        });

        assertEquals("Percentual de desconto deve estar entre 0 e 100", exception.getMessage());
    }

    @Test
    @DisplayName("Deve calcular média corretamente")
    void deveCalcularMediaCorretamente() {
        // Arrange
        List<BigDecimal> valores = Arrays.asList(
            new BigDecimal("10.00"),
            new BigDecimal("20.00"),
            new BigDecimal("30.00")
        );

        // Act
        BigDecimal resultado = calculadoraService.calcularMedia(valores);

        // Assert
        assertEquals(new BigDecimal("20.00"), resultado);
    }

    @Test
    @DisplayName("Deve retornar zero quando lista é nula")
    void deveRetornarZeroQuandoListaNula() {
        // Act
        BigDecimal resultado = calculadoraService.calcularMedia(null);

        // Assert
        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    @DisplayName("Deve retornar zero quando lista é vazia")
    void deveRetornarZeroQuandoListaVazia() {
        // Act
        BigDecimal resultado = calculadoraService.calcularMedia(Collections.emptyList());

        // Assert
        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    @DisplayName("Deve aplicar desconto corretamente")
    void deveAplicarDescontoCorretamente() {
        // Arrange
        BigDecimal valorOriginal = new BigDecimal("100.00");
        BigDecimal desconto = new BigDecimal("15.00");

        // Act
        BigDecimal resultado = calculadoraService.aplicarDesconto(valorOriginal, desconto);

        // Assert
        assertEquals(new BigDecimal("85.00"), resultado);
    }

    @Test
    @DisplayName("Deve retornar zero quando valor original é nulo")
    void deveRetornarZeroQuandoValorOriginalNulo() {
        // Arrange
        BigDecimal desconto = new BigDecimal("15.00");

        // Act
        BigDecimal resultado = calculadoraService.aplicarDesconto(null, desconto);

        // Assert
        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    @DisplayName("Deve retornar zero quando desconto é nulo")
    void deveRetornarZeroQuandoDescontoNulo() {
        // Arrange
        BigDecimal valorOriginal = new BigDecimal("100.00");

        // Act
        BigDecimal resultado = calculadoraService.aplicarDesconto(valorOriginal, null);

        // Assert
        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    @DisplayName("Deve retornar zero quando desconto é maior que valor original")
    void deveRetornarZeroQuandoDescontoMaiorQueValorOriginal() {
        // Arrange
        BigDecimal valorOriginal = new BigDecimal("50.00");
        BigDecimal desconto = new BigDecimal("75.00");

        // Act
        BigDecimal resultado = calculadoraService.aplicarDesconto(valorOriginal, desconto);

        // Assert
        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    @DisplayName("Deve verificar range corretamente")
    void deveVerificarRangeCorretamente() {
        // Arrange
        BigDecimal valor = new BigDecimal("50.00");
        BigDecimal minimo = new BigDecimal("10.00");
        BigDecimal maximo = new BigDecimal("100.00");

        // Act
        boolean resultado = calculadoraService.verificarRange(valor, minimo, maximo);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar false quando valor está fora do range")
    void deveRetornarFalseQuandoValorForaDoRange() {
        // Arrange
        BigDecimal valor = new BigDecimal("150.00");
        BigDecimal minimo = new BigDecimal("10.00");
        BigDecimal maximo = new BigDecimal("100.00");

        // Act
        boolean resultado = calculadoraService.verificarRange(valor, minimo, maximo);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve retornar false quando valor é nulo")
    void deveRetornarFalseQuandoValorNulo() {
        // Arrange
        BigDecimal minimo = new BigDecimal("10.00");
        BigDecimal maximo = new BigDecimal("100.00");

        // Act
        boolean resultado = calculadoraService.verificarRange(null, minimo, maximo);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve calcular juros compostos corretamente")
    void deveCalcularJurosCompostosCorretamente() {
        // Arrange
        BigDecimal principal = new BigDecimal("1000.00");
        BigDecimal taxa = new BigDecimal("10.00");
        int periodos = 2;

        // Act
        BigDecimal resultado = calculadoraService.calcularJurosCompostos(principal, taxa, periodos);

        // Assert
        assertEquals(new BigDecimal("1210.00"), resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando principal é nulo para juros compostos")
    void deveLancarExcecaoQuandoPrincipalNuloParaJurosCompostos() {
        // Arrange
        BigDecimal taxa = new BigDecimal("10.00");
        int periodos = 2;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadoraService.calcularJurosCompostos(null, taxa, periodos);
        });

        assertEquals("Parâmetros inválidos para cálculo de juros", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando taxa é nula para juros compostos")
    void deveLancarExcecaoQuandoTaxaNulaParaJurosCompostos() {
        // Arrange
        BigDecimal principal = new BigDecimal("1000.00");
        int periodos = 2;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadoraService.calcularJurosCompostos(principal, null, periodos);
        });

        assertEquals("Parâmetros inválidos para cálculo de juros", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando períodos é zero ou negativo")
    void deveLancarExcecaoQuandoPeriodosZeroOuNegativo() {
        // Arrange
        BigDecimal principal = new BigDecimal("1000.00");
        BigDecimal taxa = new BigDecimal("10.00");
        int periodos = 0;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculadoraService.calcularJurosCompostos(principal, taxa, periodos);
        });

        assertEquals("Parâmetros inválidos para cálculo de juros", exception.getMessage());
    }
}