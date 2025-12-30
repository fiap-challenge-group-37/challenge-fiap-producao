package com.fiap.challenge.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditoriaService - Testes")
class AuditoriaServiceTest {

    private AuditoriaService auditoriaService;

    @BeforeEach
    void setUp() {
        auditoriaService = new AuditoriaService();
    }

    @Test
    @DisplayName("Deve registrar ação de auditoria com sucesso")
    void deveRegistrarAcaoDeAuditoriaComSucesso() {
        // Arrange
        String usuarioId = "user123";
        String acao = "LOGIN";
        String recurso = "Sistema";
        String dadosAntes = "Estado anterior";
        String dadosDepois = "Estado posterior";

        // Act & Assert (não deve lançar exceção)
        assertDoesNotThrow(() -> {
            auditoriaService.registrarAcao(usuarioId, acao, recurso, dadosAntes, dadosDepois);
        });
    }

    @Test
    @DisplayName("Deve ignorar ação não monitorada")
    void deveIgnorarAcaoNaoMonitorada() {
        // Arrange
        String usuarioId = "user123";
        String acaoNaoMonitorada = "ACAO_INEXISTENTE";
        String recurso = "Recurso";

        // Act
        auditoriaService.registrarAcao(usuarioId, acaoNaoMonitorada, recurso, null, null);
        List<AuditoriaService.RegistroAuditoria> registros = auditoriaService.buscarPorUsuario(usuarioId, null, null);

        // Assert
        assertTrue(registros.isEmpty());
    }

    @Test
    @DisplayName("Deve usar 'SISTEMA' quando usuário é nulo")
    void deveUsarSistemaQuandoUsuarioNulo() {
        // Arrange
        String acao = "LOGIN";
        String recurso = "Sistema";

        // Act
        auditoriaService.registrarAcao(null, acao, recurso, null, null);
        List<AuditoriaService.RegistroAuditoria> registros = auditoriaService.buscarPorUsuario("SISTEMA", null, null);

        // Assert
        assertEquals(1, registros.size());
        assertEquals("SISTEMA", registros.get(0).getUsuarioId());
    }

    @Test
    @DisplayName("Deve registrar tentativa de ação com falha")
    void deveRegistrarTentativaDeAcaoComFalha() {
        // Arrange
        String usuarioId = "user123";
        String acao = "LOGIN";
        String recurso = "Sistema";
        String motivoFalha = "Senha inválida";

        // Act
        auditoriaService.registrarTentativaFalha(usuarioId, acao, recurso, motivoFalha);

        // Assert
        List<AuditoriaService.RegistroAuditoria> registros = auditoriaService.buscarPorUsuario(usuarioId, null, null);
        assertEquals(1, registros.size());
        assertFalse(registros.get(0).isSucesso());
        assertEquals(motivoFalha, registros.get(0).getMotivoFalha());
    }

    @Test
    @DisplayName("Deve usar 'ANONIMO' quando usuário é nulo na falha")
    void deveUsarAnonimoQuandoUsuarioNuloNaFalha() {
        // Arrange
        String acao = "LOGIN";
        String recurso = "Sistema";
        String motivoFalha = "Usuário não identificado";

        // Act
        auditoriaService.registrarTentativaFalha(null, acao, recurso, motivoFalha);

        // Assert
        List<AuditoriaService.RegistroAuditoria> registros = auditoriaService.buscarPorUsuario("ANONIMO", null, null);
        assertEquals(1, registros.size());
        assertEquals("ANONIMO", registros.get(0).getUsuarioId());
    }

    @Test
    @DisplayName("Deve buscar registros por usuário")
    void deveBuscarRegistrosPorUsuario() {
        // Arrange
        String usuarioId = "user123";
        auditoriaService.registrarAcao(usuarioId, "LOGIN", "Sistema", null, null);
        auditoriaService.registrarAcao(usuarioId, "LOGOUT", "Sistema", null, null);

        // Act
        List<AuditoriaService.RegistroAuditoria> registros = auditoriaService.buscarPorUsuario(usuarioId, null, null);

        // Assert
        assertEquals(2, registros.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia para usuário nulo")
    void deveRetornarListaVaziaParaUsuarioNulo() {
        // Act
        List<AuditoriaService.RegistroAuditoria> registros = auditoriaService.buscarPorUsuario(null, null, null);

        // Assert
        assertTrue(registros.isEmpty());
    }

    @Test
    @DisplayName("Deve filtrar registros por período")
    void deveFiltrarRegistrosPorPeriodo() {
        // Arrange
        String usuarioId = "user123";
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime ontem = agora.minusDays(1);
        LocalDateTime amanha = agora.plusDays(1);

        auditoriaService.registrarAcao(usuarioId, "LOGIN", "Sistema", null, null);

        // Act
        List<AuditoriaService.RegistroAuditoria> registrosOntemAtéAmanha = auditoriaService.buscarPorUsuario(usuarioId, ontem, amanha);
        List<AuditoriaService.RegistroAuditoria> registrosAmanha = auditoriaService.buscarPorUsuario(usuarioId, amanha, null);

        // Assert
        assertFalse(registrosOntemAtéAmanha.isEmpty()); // Deve incluir o registro de agora
        assertTrue(registrosAmanha.isEmpty()); // Não deve incluir registros de hoje
    }

    @Test
    @DisplayName("Deve buscar registros por ação específica")
    void deveBuscarRegistrosPorAcaoEspecifica() {
        // Arrange
        String usuarioId = "user123";
        auditoriaService.registrarAcao(usuarioId, "LOGIN", "Sistema", null, null);
        auditoriaService.registrarAcao(usuarioId, "LOGOUT", "Sistema", null, null);

        // Act
        List<AuditoriaService.RegistroAuditoria> registrosLogin = auditoriaService.buscarPorAcao("LOGIN", null, null);

        // Assert
        assertEquals(1, registrosLogin.size());
        assertEquals("LOGIN", registrosLogin.get(0).getAcao());
    }

    @Test
    @DisplayName("Deve retornar lista vazia para ação nula")
    void deveRetornarListaVaziaParaAcaoNula() {
        // Act
        List<AuditoriaService.RegistroAuditoria> registros = auditoriaService.buscarPorAcao(null, null, null);

        // Assert
        assertTrue(registros.isEmpty());
    }

    @Test
    @DisplayName("Deve contar tentativas de login falhadas")
    void deveContarTentativasDeLoginFalhadas() {
        // Arrange
        String usuarioId = "user123";
        auditoriaService.registrarTentativaFalha(usuarioId, "LOGIN", "Sistema", "Senha inválida");
        auditoriaService.registrarTentativaFalha(usuarioId, "LOGIN", "Sistema", "Usuário bloqueado");
        auditoriaService.registrarAcao(usuarioId, "LOGIN", "Sistema", null, null); // sucesso

        // Act
        long tentativasFalhas = auditoriaService.contarTentativasLoginFalhas(usuarioId, null);

        // Assert
        assertEquals(2, tentativasFalhas);
    }

    @Test
    @DisplayName("Deve retornar zero para usuário nulo ao contar falhas de login")
    void deveRetornarZeroParaUsuarioNuloAoContarFalhasDeLogin() {
        // Act
        long tentativasFalhas = auditoriaService.contarTentativasLoginFalhas(null, null);

        // Assert
        assertEquals(0, tentativasFalhas);
    }

    @Test
    @DisplayName("Deve adicionar ação monitorada")
    void deveAdicionarAcaoMonitorada() {
        // Arrange
        String novaAcao = "NOVA_ACAO";

        // Act
        auditoriaService.adicionarAcaoMonitorada(novaAcao);

        // Assert
        Set<String> acoesMonitoradas = auditoriaService.listarAcoesMonitoradas();
        assertTrue(acoesMonitoradas.contains(novaAcao.toUpperCase()));
    }

    @Test
    @DisplayName("Deve ignorar ação nula ao adicionar")
    void deveIgnorarAcaoNulaAoAdicionar() {
        // Arrange
        Set<String> acoesBefore = auditoriaService.listarAcoesMonitoradas();
        int tamanhoAnterior = acoesBefore.size();

        // Act
        auditoriaService.adicionarAcaoMonitorada(null);

        // Assert
        Set<String> acoesAfter = auditoriaService.listarAcoesMonitoradas();
        assertEquals(tamanhoAnterior, acoesAfter.size());
    }

    @Test
    @DisplayName("Deve ignorar ação vazia ao adicionar")
    void deveIgnorarAcaoVaziaAoAdicionar() {
        // Arrange
        Set<String> acoesBefore = auditoriaService.listarAcoesMonitoradas();
        int tamanhoAnterior = acoesBefore.size();

        // Act
        auditoriaService.adicionarAcaoMonitorada("   ");

        // Assert
        Set<String> acoesAfter = auditoriaService.listarAcoesMonitoradas();
        assertEquals(tamanhoAnterior, acoesAfter.size());
    }

    @Test
    @DisplayName("Deve remover ação monitorada")
    void deveRemoverAcaoMonitorada() {
        // Arrange
        String acao = "LOGIN";

        // Act
        boolean removida = auditoriaService.removerAcaoMonitorada(acao);

        // Assert
        assertTrue(removida);
        Set<String> acoesMonitoradas = auditoriaService.listarAcoesMonitoradas();
        assertFalse(acoesMonitoradas.contains(acao.toUpperCase()));
    }

    @Test
    @DisplayName("Deve retornar false ao tentar remover ação nula")
    void deveRetornarFalseAoTentarRemoverAcaoNula() {
        // Act
        boolean removida = auditoriaService.removerAcaoMonitorada(null);

        // Assert
        assertFalse(removida);
    }

    @Test
    @DisplayName("Deve listar ações monitoradas")
    void deveListarAcoesMonitoradas() {
        // Act
        Set<String> acoesMonitoradas = auditoriaService.listarAcoesMonitoradas();

        // Assert
        assertFalse(acoesMonitoradas.isEmpty());
        assertTrue(acoesMonitoradas.contains("LOGIN"));
        assertTrue(acoesMonitoradas.contains("LOGOUT"));
    }

    @Test
    @DisplayName("Deve gerar relatório de atividades suspeitas")
    void deveGerarRelatorioDeAtividadesSuspeitas() {
        // Arrange
        String usuarioId = "user123";
        auditoriaService.registrarTentativaFalha(usuarioId, "LOGIN", "Sistema", "Falha 1");
        auditoriaService.registrarTentativaFalha(usuarioId, "LOGIN", "Sistema", "Falha 2");
        auditoriaService.registrarTentativaFalha(usuarioId, "LOGIN", "Sistema", "Falha 3");

        // Act
        List<AuditoriaService.RegistroAuditoria> atividadesSuspeitas = auditoriaService.gerarRelatorioAtividadesSuspeitas(2);

        // Assert
        assertEquals(3, atividadesSuspeitas.size());
        assertTrue(atividadesSuspeitas.stream().allMatch(r -> !r.isSucesso()));
    }

    @Test
    @DisplayName("Deve limpar registros antigos")
    void deveLimparRegistrosAntigos() {
        // Arrange
        String usuarioId = "user123";
        auditoriaService.registrarAcao(usuarioId, "LOGIN", "Sistema", null, null);

        // Act
        int removidos = auditoriaService.limparRegistrosAntigos(0);

        // Assert
        assertTrue(removidos >= 0);
    }

    @Test
    @DisplayName("Deve retornar zero ao tentar limpar com dias negativos")
    void deveRetornarZeroAoTentarLimparComDiasNegativos() {
        // Act
        int removidos = auditoriaService.limparRegistrosAntigos(-1);

        // Assert
        assertEquals(0, removidos);
    }

    @Test
    @DisplayName("Deve filtrar registros por período na busca por ação")
    void deveFiltrarRegistrosPorPeriodoNaBuscaPorAcao() {
        // Arrange
        String usuarioId = "user123";
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime ontem = agora.minusDays(1);
        LocalDateTime amanha = agora.plusDays(1);

        auditoriaService.registrarAcao(usuarioId, "LOGIN", "Sistema", null, null);

        // Act
        List<AuditoriaService.RegistroAuditoria> registrosAmanha = auditoriaService.buscarPorAcao("LOGIN", amanha, null);

        // Assert
        assertTrue(registrosAmanha.isEmpty());
    }
}