package com.fiap.challenge.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidadorService - Testes")
class ValidadorServiceTest {

    private final ValidadorService validadorService = new ValidadorService();

    @Test
    @DisplayName("Deve validar email válido")
    void deveValidarEmailValido() {
        // Arrange
        String email = "teste@exemplo.com";

        // Act
        boolean resultado = validadorService.validarEmail(email);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar email inválido")
    void deveRejeitarEmailInvalido() {
        // Arrange
        String email = "email_invalido";

        // Act
        boolean resultado = validadorService.validarEmail(email);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar email nulo")
    void deveRejeitarEmailNulo() {
        // Act
        boolean resultado = validadorService.validarEmail(null);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar email vazio")
    void deveRejeitarEmailVazio() {
        // Act
        boolean resultado = validadorService.validarEmail("   ");

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve validar telefone válido com 10 dígitos")
    void deveValidarTelefoneValidoCom10Digitos() {
        // Arrange
        String telefone = "(11) 1234-5678";

        // Act
        boolean resultado = validadorService.validarTelefone(telefone);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve validar telefone válido com 11 dígitos")
    void deveValidarTelefoneValidoCom11Digitos() {
        // Arrange
        String telefone = "(11) 91234-5678";

        // Act
        boolean resultado = validadorService.validarTelefone(telefone);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar telefone inválido")
    void deveRejeitarTelefoneInvalido() {
        // Arrange
        String telefone = "123";

        // Act
        boolean resultado = validadorService.validarTelefone(telefone);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar telefone nulo")
    void deveRejeitarTelefoneNulo() {
        // Act
        boolean resultado = validadorService.validarTelefone(null);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve validar tamanho mínimo correto")
    void deveValidarTamanhoMinimoCorreto() {
        // Arrange
        String texto = "abcdef";
        int tamanhoMinimo = 5;

        // Act
        boolean resultado = validadorService.validarTamanhoMinimo(texto, tamanhoMinimo);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar texto menor que tamanho mínimo")
    void deveRejeitarTextoMenorQueTamanhoMinimo() {
        // Arrange
        String texto = "abc";
        int tamanhoMinimo = 5;

        // Act
        boolean resultado = validadorService.validarTamanhoMinimo(texto, tamanhoMinimo);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar texto nulo para tamanho mínimo")
    void deveRejeitarTextoNuloParaTamanhoMinimo() {
        // Arrange
        int tamanhoMinimo = 5;

        // Act
        boolean resultado = validadorService.validarTamanhoMinimo(null, tamanhoMinimo);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve validar tamanho dentro do range")
    void deveValidarTamanhoDentroDoRange() {
        // Arrange
        String texto = "abcdef";
        int minimo = 5;
        int maximo = 10;

        // Act
        boolean resultado = validadorService.validarTamanhoRange(texto, minimo, maximo);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar tamanho fora do range")
    void deveRejeitarTamanhoForaDoRange() {
        // Arrange
        String texto = "abc";
        int minimo = 5;
        int maximo = 10;

        // Act
        boolean resultado = validadorService.validarTamanhoRange(texto, minimo, maximo);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve validar número dentro do range")
    void deveValidarNumeroDentroDoRange() {
        // Arrange
        Integer numero = 7;
        int minimo = 5;
        int maximo = 10;

        // Act
        boolean resultado = validadorService.validarNumeroRange(numero, minimo, maximo);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar número fora do range")
    void deveRejeitarNumeroForaDoRange() {
        // Arrange
        Integer numero = 3;
        int minimo = 5;
        int maximo = 10;

        // Act
        boolean resultado = validadorService.validarNumeroRange(numero, minimo, maximo);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar número nulo")
    void deveRejeitarNumeroNulo() {
        // Arrange
        int minimo = 5;
        int maximo = 10;

        // Act
        boolean resultado = validadorService.validarNumeroRange(null, minimo, maximo);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve validar texto com apenas números")
    void deveValidarTextoComApenasNumeros() {
        // Arrange
        String texto = "12345";

        // Act
        boolean resultado = validadorService.validarApenasNumeros(texto);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar texto com letras")
    void deveRejeitarTextoComLetras() {
        // Arrange
        String texto = "123abc";

        // Act
        boolean resultado = validadorService.validarApenasNumeros(texto);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar texto nulo para validação de números")
    void deveRejeitarTextoNuloParaValidacaoDeNumeros() {
        // Act
        boolean resultado = validadorService.validarApenasNumeros(null);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve rejeitar texto vazio para validação de números")
    void deveRejeitarTextoVazioParaValidacaoDeNumeros() {
        // Act
        boolean resultado = validadorService.validarApenasNumeros("   ");

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve retornar senha inválida para senha nula")
    void deveRetornarSenhaInvalidaParaSenhaNula() {
        // Act
        ValidadorService.TipoForcaSenha resultado = validadorService.validarForcaSenha(null);

        // Assert
        assertEquals(ValidadorService.TipoForcaSenha.INVALIDA, resultado);
    }

    @Test
    @DisplayName("Deve retornar senha inválida para senha vazia")
    void deveRetornarSenhaInvalidaParaSenhaVazia() {
        // Act
        ValidadorService.TipoForcaSenha resultado = validadorService.validarForcaSenha("");

        // Assert
        assertEquals(ValidadorService.TipoForcaSenha.INVALIDA, resultado);
    }

    @Test
    @DisplayName("Deve retornar senha fraca")
    void deveRetornarSenhaFraca() {
        // Arrange
        String senha = "123";

        // Act
        ValidadorService.TipoForcaSenha resultado = validadorService.validarForcaSenha(senha);

        // Assert
        assertEquals(ValidadorService.TipoForcaSenha.FRACA, resultado);
    }

    @Test
    @DisplayName("Deve retornar senha média")
    void deveRetornarSenhaMedia() {
        // Arrange
        String senha = "12345678a";

        // Act
        ValidadorService.TipoForcaSenha resultado = validadorService.validarForcaSenha(senha);

        // Assert
        assertEquals(ValidadorService.TipoForcaSenha.MEDIA, resultado);
    }

    @Test
    @DisplayName("Deve retornar senha forte")
    void deveRetornarSenhaForte() {
        // Arrange
        String senha = "MinhaSenh@123";

        // Act
        ValidadorService.TipoForcaSenha resultado = validadorService.validarForcaSenha(senha);

        // Assert
        assertEquals(ValidadorService.TipoForcaSenha.FORTE, resultado);
    }
}