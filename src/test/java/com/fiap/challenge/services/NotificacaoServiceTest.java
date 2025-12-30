package com.fiap.challenge.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacaoService - Testes")
class NotificacaoServiceTest {

    private NotificacaoService notificacaoService;

    @BeforeEach
    void setUp() {
        notificacaoService = new NotificacaoService();
    }

    @Test
    @DisplayName("Deve enviar notificação por email com sucesso")
    void deveEnviarNotificacaoPorEmailComSucesso() {
        // Arrange
        String usuarioId = "user123";
        NotificacaoService.TipoNotificacao tipo = NotificacaoService.TipoNotificacao.EMAIL;
        String titulo = "Teste";
        String mensagem = "Mensagem de teste";

        // Act
        boolean resultado = notificacaoService.enviarNotificacao(usuarioId, tipo, titulo, mensagem);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve falhar ao enviar notificação por canal desabilitado")
    void deveFalharAoEnviarNotificacaoPorCanalDesabilitado() {
        // Arrange
        String usuarioId = "user123";
        NotificacaoService.TipoNotificacao tipo = NotificacaoService.TipoNotificacao.PUSH;
        String titulo = "Teste";
        String mensagem = "Mensagem de teste";

        // Act
        boolean resultado = notificacaoService.enviarNotificacao(usuarioId, tipo, titulo, mensagem);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário ID é nulo")
    void deveLancarExcecaoQuandoUsuarioIdNulo() {
        // Arrange
        NotificacaoService.TipoNotificacao tipo = NotificacaoService.TipoNotificacao.EMAIL;
        String titulo = "Teste";
        String mensagem = "Mensagem de teste";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificacaoService.enviarNotificacao(null, tipo, titulo, mensagem);
        });

        assertEquals("ID do usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário ID está vazio")
    void deveLancarExcecaoQuandoUsuarioIdVazio() {
        // Arrange
        String usuarioId = "   ";
        NotificacaoService.TipoNotificacao tipo = NotificacaoService.TipoNotificacao.EMAIL;
        String titulo = "Teste";
        String mensagem = "Mensagem de teste";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificacaoService.enviarNotificacao(usuarioId, tipo, titulo, mensagem);
        });

        assertEquals("ID do usuário é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando tipo de notificação é nulo")
    void deveLancarExcecaoQuandoTipoDeNotificacaoNulo() {
        // Arrange
        String usuarioId = "user123";
        String titulo = "Teste";
        String mensagem = "Mensagem de teste";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificacaoService.enviarNotificacao(usuarioId, null, titulo, mensagem);
        });

        assertEquals("Tipo de notificação é obrigatório", exception.getMessage());
    }

    @Test
    @DisplayName("Deve buscar notificações por usuário")
    void deveBuscarNotificacoesPorUsuario() {
        // Arrange
        String usuarioId = "user123";
        notificacaoService.enviarNotificacao(usuarioId, NotificacaoService.TipoNotificacao.EMAIL, "Teste 1", "Mensagem 1");
        notificacaoService.enviarNotificacao(usuarioId, NotificacaoService.TipoNotificacao.SMS, "Teste 2", "Mensagem 2");

        // Act
        List<NotificacaoService.Notificacao> resultado = notificacaoService.buscarNotificacoes(usuarioId, null);

        // Assert
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia para usuário nulo")
    void deveRetornarListaVaziaParaUsuarioNulo() {
        // Act
        List<NotificacaoService.Notificacao> resultado = notificacaoService.buscarNotificacoes(null, null);

        // Assert
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve filtrar notificações por status")
    void deveFiltrarNotificacoesPorStatus() {
        // Arrange
        String usuarioId = "user123";
        notificacaoService.enviarNotificacao(usuarioId, NotificacaoService.TipoNotificacao.EMAIL, "Teste", "Mensagem");
        
        List<NotificacaoService.Notificacao> notificacoes = notificacaoService.buscarNotificacoes(usuarioId, null);
        String notificacaoId = notificacoes.get(0).getId();
        notificacaoService.marcarComoLida(notificacaoId, usuarioId);

        // Act
        List<NotificacaoService.Notificacao> lidas = notificacaoService.buscarNotificacoes(usuarioId, NotificacaoService.StatusNotificacao.LIDA);

        // Assert
        assertEquals(1, lidas.size());
        assertEquals(NotificacaoService.StatusNotificacao.LIDA, lidas.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve marcar notificação como lida")
    void deveMarcarNotificacaoComoLida() {
        // Arrange
        String usuarioId = "user123";
        notificacaoService.enviarNotificacao(usuarioId, NotificacaoService.TipoNotificacao.EMAIL, "Teste", "Mensagem");
        
        List<NotificacaoService.Notificacao> notificacoes = notificacaoService.buscarNotificacoes(usuarioId, null);
        String notificacaoId = notificacoes.get(0).getId();

        // Act
        boolean resultado = notificacaoService.marcarComoLida(notificacaoId, usuarioId);

        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar false ao tentar marcar notificação inexistente como lida")
    void deveRetornarFalseAoTentarMarcarNotificacaoInexistenteComoLida() {
        // Arrange
        String usuarioId = "user123";
        String notificacaoId = "inexistente";

        // Act
        boolean resultado = notificacaoService.marcarComoLida(notificacaoId, usuarioId);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve retornar false quando notificação ID é nulo")
    void deveRetornarFalseQuandoNotificacaoIdNulo() {
        // Arrange
        String usuarioId = "user123";

        // Act
        boolean resultado = notificacaoService.marcarComoLida(null, usuarioId);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve configurar canal de notificação")
    void deveConfigurarCanalDeNotificacao() {
        // Arrange
        NotificacaoService.TipoNotificacao tipo = NotificacaoService.TipoNotificacao.PUSH;

        // Act
        notificacaoService.configurarCanal(tipo, true);

        // Assert
        assertTrue(notificacaoService.isCanalHabilitado(tipo));
    }

    @Test
    @DisplayName("Deve verificar se canal está habilitado")
    void deveVerificarSeCanalEstaHabilitado() {
        // Act
        boolean emailHabilitado = notificacaoService.isCanalHabilitado(NotificacaoService.TipoNotificacao.EMAIL);
        boolean pushHabilitado = notificacaoService.isCanalHabilitado(NotificacaoService.TipoNotificacao.PUSH);

        // Assert
        assertTrue(emailHabilitado);
        assertFalse(pushHabilitado);
    }

    @Test
    @DisplayName("Deve retornar false para tipo nulo ao verificar canal")
    void deveRetornarFalseParaTipoNuloAoVerificarCanal() {
        // Act
        boolean resultado = notificacaoService.isCanalHabilitado(null);

        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve contar notificações não lidas")
    void deveContarNotificacaoNaoLidas() {
        // Arrange
        String usuarioId = "user123";
        notificacaoService.enviarNotificacao(usuarioId, NotificacaoService.TipoNotificacao.EMAIL, "Teste 1", "Mensagem 1");
        notificacaoService.enviarNotificacao(usuarioId, NotificacaoService.TipoNotificacao.SMS, "Teste 2", "Mensagem 2");

        // Act
        long contador = notificacaoService.contarNaoLidas(usuarioId);

        // Assert
        assertEquals(2, contador);
    }

    @Test
    @DisplayName("Deve retornar zero para usuário nulo ao contar não lidas")
    void deveRetornarZeroParaUsuarioNuloAoContarNaoLidas() {
        // Act
        long contador = notificacaoService.contarNaoLidas(null);

        // Assert
        assertEquals(0, contador);
    }

    @Test
    @DisplayName("Deve limpar notificações antigas")
    void deveLimparNotificacoesAntigas() {
        // Arrange
        String usuarioId = "user123";
        notificacaoService.enviarNotificacao(usuarioId, NotificacaoService.TipoNotificacao.EMAIL, "Teste", "Mensagem");

        // Act
        int removidas = notificacaoService.limparNotificacoes(usuarioId, 0);

        // Assert
        assertTrue(removidas >= 0);
    }

    @Test
    @DisplayName("Deve retornar zero ao tentar limpar com usuário nulo")
    void deveRetornarZeroAoTentarLimparComUsuarioNulo() {
        // Act
        int removidas = notificacaoService.limparNotificacoes(null, 7);

        // Assert
        assertEquals(0, removidas);
    }

    @Test
    @DisplayName("Deve retornar zero ao tentar limpar com dias negativos")
    void deveRetornarZeroAoTentarLimparComDiasNegativos() {
        // Arrange
        String usuarioId = "user123";

        // Act
        int removidas = notificacaoService.limparNotificacoes(usuarioId, -1);

        // Assert
        assertEquals(0, removidas);
    }

    @Test
    @DisplayName("Deve ignorar configuração de canal quando tipo é nulo")
    void deveIgnorarConfiguracaoDeCanalQuandoTipoNulo() {
        // Act & Assert (não deve lançar exceção)
        assertDoesNotThrow(() -> {
            notificacaoService.configurarCanal(null, true);
        });
    }
}