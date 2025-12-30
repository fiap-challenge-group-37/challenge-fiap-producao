package com.fiap.challenge.cliente.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class CpfTest {

    @Test
    @DisplayName("Deve criar CPF válido")
    void deveCriarCpfValido() {
        Cpf cpf = new Cpf("52998224725");
        assertEquals("52998224725", cpf.getValue());
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF inválido")
    void deveLancarExcecao_ParaCpfInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cpf("12345678901"));
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF nulo")
    void deveLancarExcecao_ParaCpfNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cpf(null));
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF com dígitos repetidos")
    void deveLancarExcecao_ParaCpfComDigitosRepetidos() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cpf("11111111111"));
    }

    @Test
    @DisplayName("Deve implementar toString corretamente")
    void deveImplementarToStringCorretamente() {
        Cpf cpf = new Cpf("52998224725");
        
        String resultado = cpf.toString();
        
        assertEquals("52998224725", resultado);
    }

    @Test
    @DisplayName("Deve implementar equals corretamente - objetos iguais")
    void deveImplementarEqualsCorretamenteObjetosIguais() {
        Cpf cpf1 = new Cpf("52998224725");
        Cpf cpf2 = new Cpf("52998224725");
        
        assertTrue(cpf1.equals(cpf2));
        assertTrue(cpf2.equals(cpf1));
    }

    @Test
    @DisplayName("Deve implementar equals corretamente - objetos diferentes")
    void deveImplementarEqualsCorretamenteObjetosDiferentes() {
        Cpf cpf1 = new Cpf("52998224725");
        Cpf cpf2 = new Cpf("11144477735");
        
        assertFalse(cpf1.equals(cpf2));
        assertFalse(cpf2.equals(cpf1));
    }

    @Test
    @DisplayName("Deve implementar equals corretamente - comparação com null")
    void deveImplementarEqualsCorretamenteComparacaoComNull() {
        Cpf cpf = new Cpf("52998224725");
        
        assertFalse(cpf.equals(null));
    }

    @Test
    @DisplayName("Deve implementar equals corretamente - comparação com objeto de classe diferente")
    void deveImplementarEqualsCorretamenteComparacaoComClasseDiferente() {
        Cpf cpf = new Cpf("52998224725");
        String string = "52998224725";
        
        assertFalse(cpf.equals(string));
    }

    @Test
    @DisplayName("Deve implementar hashCode corretamente - objetos iguais têm mesmo hashCode")
    void deveImplementarHashCodeCorretamenteObjetosIguais() {
        Cpf cpf1 = new Cpf("52998224725");
        Cpf cpf2 = new Cpf("52998224725");
        
        assertEquals(cpf1.hashCode(), cpf2.hashCode());
    }
}
