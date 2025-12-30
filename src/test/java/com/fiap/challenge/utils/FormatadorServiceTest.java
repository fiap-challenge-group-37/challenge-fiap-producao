package com.fiap.challenge.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FormatadorService - Testes")
class FormatadorServiceTest {

    private final FormatadorService formatadorService = new FormatadorService();

    @Test
    @DisplayName("Deve formatar CPF corretamente")
    void deveFormatarCpfCorretamente() {
        // Arrange
        String cpf = "12345678901";

        // Act
        String resultado = formatadorService.formatarCpf(cpf);

        // Assert
        assertEquals("123.456.789-01", resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF nulo")
    void deveLancarExcecaoParaCpfNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            formatadorService.formatarCpf(null);
        });

        assertEquals("CPF deve conter exatamente 11 dígitos numéricos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF com tamanho incorreto")
    void deveLancarExcecaoParaCpfComTamanhoIncorreto() {
        // Arrange
        String cpf = "123456789";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            formatadorService.formatarCpf(cpf);
        });

        assertEquals("CPF deve conter exatamente 11 dígitos numéricos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF com caracteres não numéricos")
    void deveLancarExcecaoParaCpfComCaracteresNaoNumericos() {
        // Arrange
        String cpf = "1234567890a";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            formatadorService.formatarCpf(cpf);
        });

        assertEquals("CPF deve conter exatamente 11 dígitos numéricos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve limpar CPF formatado")
    void deveLimparCpfFormatado() {
        // Arrange
        String cpfFormatado = "123.456.789-01";

        // Act
        String resultado = formatadorService.limparCpf(cpfFormatado);

        // Assert
        assertEquals("12345678901", resultado);
    }

    @Test
    @DisplayName("Deve retornar null para CPF nulo na limpeza")
    void deveRetornarNullParaCpfNuloNaLimpeza() {
        // Act
        String resultado = formatadorService.limparCpf(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve formatar CNPJ corretamente")
    void deveFormatarCnpjCorretamente() {
        // Arrange
        String cnpj = "12345678000195";

        // Act
        String resultado = formatadorService.formatarCnpj(cnpj);

        // Assert
        assertEquals("12.345.678/0001-95", resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção para CNPJ nulo")
    void deveLancarExcecaoParaCnpjNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            formatadorService.formatarCnpj(null);
        });

        assertEquals("CNPJ deve conter exatamente 14 dígitos numéricos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção para CNPJ com tamanho incorreto")
    void deveLancarExcecaoParaCnpjComTamanhoIncorreto() {
        // Arrange
        String cnpj = "123456780001";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            formatadorService.formatarCnpj(cnpj);
        });

        assertEquals("CNPJ deve conter exatamente 14 dígitos numéricos", exception.getMessage());
    }

    @Test
    @DisplayName("Deve formatar telefone com 10 dígitos")
    void deveFormatarTelefoneCom10Digitos() {
        // Arrange
        String telefone = "1112345678";

        // Act
        String resultado = formatadorService.formatarTelefone(telefone);

        // Assert
        assertEquals("(11) 1234-5678", resultado);
    }

    @Test
    @DisplayName("Deve formatar telefone com 11 dígitos")
    void deveFormatarTelefoneCom11Digitos() {
        // Arrange
        String telefone = "11912345678";

        // Act
        String resultado = formatadorService.formatarTelefone(telefone);

        // Assert
        assertEquals("(11) 91234-5678", resultado);
    }

    @Test
    @DisplayName("Deve retornar telefone original quando formato inválido")
    void deveRetornarTelefoneOriginalQuandoFormatoInvalido() {
        // Arrange
        String telefone = "123";

        // Act
        String resultado = formatadorService.formatarTelefone(telefone);

        // Assert
        assertEquals("123", resultado);
    }

    @Test
    @DisplayName("Deve retornar null para telefone nulo")
    void deveRetornarNullParaTelefoneNulo() {
        // Act
        String resultado = formatadorService.formatarTelefone(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve formatar data e hora brasileira")
    void deveFormatarDataEHoraBrasileira() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.of(2024, 12, 31, 23, 59, 59);

        // Act
        String resultado = formatadorService.formatarDataHoraBrasileira(dataHora);

        // Assert
        assertEquals("31/12/2024 23:59:59", resultado);
    }

    @Test
    @DisplayName("Deve retornar null para data nula no formato brasileiro")
    void deveRetornarNullParaDataNulaNoFormatoBrasileiro() {
        // Act
        String resultado = formatadorService.formatarDataHoraBrasileira(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve formatar data e hora ISO")
    void deveFormatarDataEHoraISO() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.of(2024, 12, 31, 23, 59, 59);

        // Act
        String resultado = formatadorService.formatarDataHoraISO(dataHora);

        // Assert
        assertEquals("2024-12-31T23:59:59", resultado);
    }

    @Test
    @DisplayName("Deve retornar null para data nula no formato ISO")
    void deveRetornarNullParaDataNulaNoFormatoISO() {
        // Act
        String resultado = formatadorService.formatarDataHoraISO(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve formatar texto como título")
    void deveFormatarTextoComoTitulo() {
        // Arrange
        String texto = "josé da silva";

        // Act
        String resultado = formatadorService.formatarTitulo(texto);

        // Assert
        assertEquals("José Da Silva", resultado);
    }

    @Test
    @DisplayName("Deve lidar com texto com espaços extras")
    void deveLidarComTextoComEspacosExtras() {
        // Arrange
        String texto = "  josé   da   silva  ";

        // Act
        String resultado = formatadorService.formatarTitulo(texto);

        // Assert
        assertEquals("José Da Silva", resultado);
    }

    @Test
    @DisplayName("Deve retornar texto original quando nulo para título")
    void deveRetornarTextoOriginalQuandoNuloParaTitulo() {
        // Act
        String resultado = formatadorService.formatarTitulo(null);

        // Assert
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve retornar texto original quando vazio para título")
    void deveRetornarTextoOriginalQuandoVazioParaTitulo() {
        // Act
        String resultado = formatadorService.formatarTitulo("   ");

        // Assert
        assertEquals("   ", resultado);
    }

    @Test
    @DisplayName("Deve mascarar informação corretamente")
    void deveMascararInformacaoCorretamente() {
        // Arrange
        String informacao = "12345678901";
        int caracteresVisiveis = 3;

        // Act
        String resultado = formatadorService.mascararInformacao(informacao, caracteresVisiveis);

        // Assert
        assertEquals("123*****901", resultado);
    }

    @Test
    @DisplayName("Deve retornar informação original quando muito pequena para mascarar")
    void deveRetornarInformacaoOriginalQuandoMuitoPequenaParaMascarar() {
        // Arrange
        String informacao = "123";
        int caracteresVisiveis = 3;

        // Act
        String resultado = formatadorService.mascararInformacao(informacao, caracteresVisiveis);

        // Assert
        assertEquals("123", resultado);
    }

    @Test
    @DisplayName("Deve retornar informação original quando nula para mascarar")
    void deveRetornarInformacaoOriginalQuandoNulaParaMascarar() {
        // Arrange
        int caracteresVisiveis = 3;

        // Act
        String resultado = formatadorService.mascararInformacao(null, caracteresVisiveis);

        // Assert
        assertNull(resultado);
    }
}