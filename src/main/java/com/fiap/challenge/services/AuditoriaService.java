package com.fiap.challenge.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço de auditoria para registrar ações do sistema
 */
@Service
public class AuditoriaService {
    
    private final Map<String, List<RegistroAuditoria>> registrosPorUsuario = new ConcurrentHashMap<>();
    private final Set<String> acoesMonitoradas = new HashSet<>();
    
    public AuditoriaService() {
        // Inicializa ações monitoradas
        acoesMonitoradas.addAll(Arrays.asList(
            "LOGIN", "LOGOUT", "CRIAR_PRODUTO", "EDITAR_PRODUTO", 
            "EXCLUIR_PRODUTO", "CRIAR_PEDIDO", "CANCELAR_PEDIDO"
        ));
    }
    
    /**
     * Registra uma ação de auditoria
     */
    public void registrarAcao(String usuarioId, String acao, String recurso, Object dadosAntes, Object dadosDepois) {
        if (!isAcaoMonitorada(acao)) {
            return;
        }
        
        RegistroAuditoria registro = new RegistroAuditoria();
        registro.setId(UUID.randomUUID().toString());
        registro.setUsuarioId(usuarioId != null ? usuarioId : "SISTEMA");
        registro.setAcao(acao);
        registro.setRecurso(recurso);
        registro.setDadosAntes(dadosAntes != null ? dadosAntes.toString() : null);
        registro.setDadosDepois(dadosDepois != null ? dadosDepois.toString() : null);
        registro.setDataHora(LocalDateTime.now());
        registro.setIpOrigem(obterIpSimulado());
        registro.setSucesso(true);
        
        registrosPorUsuario.computeIfAbsent(registro.getUsuarioId(), k -> new ArrayList<>()).add(registro);
    }
    
    /**
     * Registra uma tentativa de ação com falha
     */
    public void registrarTentativaFalha(String usuarioId, String acao, String recurso, String motivoFalha) {
        if (!isAcaoMonitorada(acao)) {
            return;
        }
        
        RegistroAuditoria registro = new RegistroAuditoria();
        registro.setId(UUID.randomUUID().toString());
        registro.setUsuarioId(usuarioId != null ? usuarioId : "ANONIMO");
        registro.setAcao(acao);
        registro.setRecurso(recurso);
        registro.setMotivoFalha(motivoFalha);
        registro.setDataHora(LocalDateTime.now());
        registro.setIpOrigem(obterIpSimulado());
        registro.setSucesso(false);
        
        registrosPorUsuario.computeIfAbsent(registro.getUsuarioId(), k -> new ArrayList<>()).add(registro);
    }
    
    /**
     * Busca registros de auditoria por usuário
     */
    public List<RegistroAuditoria> buscarPorUsuario(String usuarioId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (usuarioId == null) {
            return Collections.emptyList();
        }
        
        List<RegistroAuditoria> registros = registrosPorUsuario.getOrDefault(usuarioId, Collections.emptyList());
        
        if (dataInicio == null && dataFim == null) {
            return new ArrayList<>(registros);
        }
        
        return registros.stream()
                .filter(r -> filtrarPorPeriodo(r, dataInicio, dataFim))
                .toList();
    }
    
    /**
     * Busca registros por ação específica
     */
    public List<RegistroAuditoria> buscarPorAcao(String acao, LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (acao == null) {
            return Collections.emptyList();
        }
        
        return registrosPorUsuario.values().stream()
                .flatMap(List::stream)
                .filter(r -> acao.equals(r.getAcao()))
                .filter(r -> filtrarPorPeriodo(r, dataInicio, dataFim))
                .toList();
    }
    
    /**
     * Conta tentativas de login falhadas por usuário
     */
    public long contarTentativasLoginFalhas(String usuarioId, LocalDateTime dataInicio) {
        if (usuarioId == null) {
            return 0;
        }
        
        return registrosPorUsuario.getOrDefault(usuarioId, Collections.emptyList())
                .stream()
                .filter(r -> "LOGIN".equals(r.getAcao()))
                .filter(r -> !r.isSucesso())
                .filter(r -> dataInicio == null || r.getDataHora().isAfter(dataInicio))
                .count();
    }
    
    /**
     * Adiciona uma nova ação para monitoramento
     */
    public void adicionarAcaoMonitorada(String acao) {
        if (acao != null && !acao.trim().isEmpty()) {
            acoesMonitoradas.add(acao.trim().toUpperCase());
        }
    }
    
    /**
     * Remove uma ação do monitoramento
     */
    public boolean removerAcaoMonitorada(String acao) {
        return acao != null && acoesMonitoradas.remove(acao.toUpperCase());
    }
    
    /**
     * Lista todas as ações monitoradas
     */
    public Set<String> listarAcoesMonitoradas() {
        return new HashSet<>(acoesMonitoradas);
    }
    
    /**
     * Gera relatório de atividades suspeitas
     */
    public List<RegistroAuditoria> gerarRelatorioAtividadesSuspeitas(int limiteTentativasFalhas) {
        Map<String, Long> tentativasPorUsuario = new HashMap<>();
        
        // Conta tentativas de login falhadas por usuário
        registrosPorUsuario.forEach((usuario, registros) -> {
            long falhas = registros.stream()
                    .filter(r -> "LOGIN".equals(r.getAcao()) && !r.isSucesso())
                    .count();
            tentativasPorUsuario.put(usuario, falhas);
        });
        
        // Retorna usuários com muitas tentativas falhas
        return tentativasPorUsuario.entrySet().stream()
                .filter(entry -> entry.getValue() >= limiteTentativasFalhas)
                .flatMap(entry -> registrosPorUsuario.get(entry.getKey()).stream())
                .filter(r -> !r.isSucesso())
                .toList();
    }
    
    /**
     * Limpa registros antigos
     */
    public int limparRegistrosAntigos(int diasParaManter) {
        if (diasParaManter < 0) {
            return 0;
        }
        
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(diasParaManter);
        int totalRemovido = 0;
        
        for (List<RegistroAuditoria> registros : registrosPorUsuario.values()) {
            int tamanhoAnterior = registros.size();
            registros.removeIf(r -> r.getDataHora().isBefore(dataLimite));
            totalRemovido += tamanhoAnterior - registros.size();
        }
        
        return totalRemovido;
    }
    
    private boolean isAcaoMonitorada(String acao) {
        return acao != null && acoesMonitoradas.contains(acao.toUpperCase());
    }
    
    private boolean filtrarPorPeriodo(RegistroAuditoria registro, LocalDateTime dataInicio, LocalDateTime dataFim) {
        LocalDateTime dataRegistro = registro.getDataHora();
        
        if (dataInicio != null && dataRegistro.isBefore(dataInicio)) {
            return false;
        }
        
        if (dataFim != null && dataRegistro.isAfter(dataFim)) {
            return false;
        }
        
        return true;
    }
    
    private String obterIpSimulado() {
        Random random = new Random();
        return String.format("192.168.%d.%d", 
            random.nextInt(256), 
            random.nextInt(256));
    }
    
    public static class RegistroAuditoria {
        private String id;
        private String usuarioId;
        private String acao;
        private String recurso;
        private String dadosAntes;
        private String dadosDepois;
        private String motivoFalha;
        private LocalDateTime dataHora;
        private String ipOrigem;
        private boolean sucesso;
        
        // Getters e setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getUsuarioId() { return usuarioId; }
        public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
        
        public String getAcao() { return acao; }
        public void setAcao(String acao) { this.acao = acao; }
        
        public String getRecurso() { return recurso; }
        public void setRecurso(String recurso) { this.recurso = recurso; }
        
        public String getDadosAntes() { return dadosAntes; }
        public void setDadosAntes(String dadosAntes) { this.dadosAntes = dadosAntes; }
        
        public String getDadosDepois() { return dadosDepois; }
        public void setDadosDepois(String dadosDepois) { this.dadosDepois = dadosDepois; }
        
        public String getMotivoFalha() { return motivoFalha; }
        public void setMotivoFalha(String motivoFalha) { this.motivoFalha = motivoFalha; }
        
        public LocalDateTime getDataHora() { return dataHora; }
        public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
        
        public String getIpOrigem() { return ipOrigem; }
        public void setIpOrigem(String ipOrigem) { this.ipOrigem = ipOrigem; }
        
        public boolean isSucesso() { return sucesso; }
        public void setSucesso(boolean sucesso) { this.sucesso = sucesso; }
    }
}