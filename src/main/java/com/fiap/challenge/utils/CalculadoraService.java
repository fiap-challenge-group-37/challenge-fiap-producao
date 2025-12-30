package com.fiap.challenge.utils;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Serviço utilitário para operações matemáticas e cálculos
 */
@Service
public class CalculadoraService {
    
    /**
     * Calcula o desconto baseado no valor total e percentual
     */
    public BigDecimal calcularDesconto(BigDecimal valorTotal, BigDecimal percentualDesconto) {
        if (valorTotal == null || percentualDesconto == null) {
            throw new IllegalArgumentException("Valor total e percentual de desconto não podem ser nulos");
        }
        
        if (valorTotal.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor total não pode ser negativo");
        }
        
        if (percentualDesconto.compareTo(BigDecimal.ZERO) < 0 || percentualDesconto.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Percentual de desconto deve estar entre 0 e 100");
        }
        
        return valorTotal.multiply(percentualDesconto)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula a média aritmética de uma lista de valores
     */
    public BigDecimal calcularMedia(List<BigDecimal> valores) {
        if (valores == null || valores.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal soma = valores.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return soma.divide(new BigDecimal(valores.size()), 2, RoundingMode.HALF_UP);
    }
    
    /**
     * Calcula o valor final com desconto aplicado
     */
    public BigDecimal aplicarDesconto(BigDecimal valorOriginal, BigDecimal desconto) {
        if (valorOriginal == null || desconto == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal valorFinal = valorOriginal.subtract(desconto);
        return valorFinal.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : valorFinal;
    }
    
    /**
     * Verifica se um valor está dentro de um range específico
     */
    public boolean verificarRange(BigDecimal valor, BigDecimal minimo, BigDecimal maximo) {
        if (valor == null || minimo == null || maximo == null) {
            return false;
        }
        
        return valor.compareTo(minimo) >= 0 && valor.compareTo(maximo) <= 0;
    }
    
    /**
     * Calcula juros compostos
     */
    public BigDecimal calcularJurosCompostos(BigDecimal principal, BigDecimal taxa, int periodos) {
        if (principal == null || taxa == null || periodos <= 0) {
            throw new IllegalArgumentException("Parâmetros inválidos para cálculo de juros");
        }
        
        BigDecimal um = BigDecimal.ONE;
        BigDecimal taxaDecimal = taxa.divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
        BigDecimal fator = um.add(taxaDecimal);
        
        BigDecimal resultado = principal;
        for (int i = 0; i < periodos; i++) {
            resultado = resultado.multiply(fator);
        }
        
        return resultado.setScale(2, RoundingMode.HALF_UP);
    }
}