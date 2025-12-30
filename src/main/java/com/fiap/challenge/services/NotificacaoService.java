package com.fiap.challenge.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço para envio de notificações
 */
@Service
public class NotificacaoService {
    
    private final Map<String, List<Notificacao>> notificacoesPorUsuario = new ConcurrentHashMap<>();
    private final Map<TipoNotificacao, Boolean> canaisHabilitados = new HashMap<>();
    
    public NotificacaoService() {
        // Inicializa canais habilitados
        canaisHabilitados.put(TipoNotificacao.EMAIL, true);
        canaisHabilitados.put(TipoNotificacao.SMS, true);
        canaisHabilitados.put(TipoNotificacao.PUSH, false);
        canaisHabilitados.put(TipoNotificacao.WHATSAPP, false);
    }
    
    /**
     * Envia notificação para um usuário
     */
    public boolean enviarNotificacao(String usuarioId, TipoNotificacao tipo, String titulo, String mensagem) {
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }
        
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de notificação é obrigatório");
        }
        
        if (!canaisHabilitados.getOrDefault(tipo, false)) {
            return false;
        }
        
        Notificacao notificacao = new Notificacao();
        notificacao.setId(UUID.randomUUID().toString());
        notificacao.setUsuarioId(usuarioId);
        notificacao.setTipo(tipo);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setStatus(StatusNotificacao.ENVIADA);
        
        notificacoesPorUsuario.computeIfAbsent(usuarioId, k -> new ArrayList<>()).add(notificacao);
        
        return simularEnvio(tipo);
    }
    
    /**
     * Busca notificações de um usuário
     */
    public List<Notificacao> buscarNotificacoes(String usuarioId, StatusNotificacao status) {
        if (usuarioId == null) {
            return Collections.emptyList();
        }
        
        List<Notificacao> todasNotificacoes = notificacoesPorUsuario.getOrDefault(usuarioId, Collections.emptyList());
        
        if (status == null) {
            return new ArrayList<>(todasNotificacoes);
        }
        
        return todasNotificacoes.stream()
                .filter(n -> n.getStatus() == status)
                .toList();
    }
    
    /**
     * Marca notificação como lida
     */
    public boolean marcarComoLida(String notificacaoId, String usuarioId) {
        if (notificacaoId == null || usuarioId == null) {
            return false;
        }
        
        List<Notificacao> notificacoes = notificacoesPorUsuario.get(usuarioId);
        if (notificacoes == null) {
            return false;
        }
        
        return notificacoes.stream()
                .filter(n -> n.getId().equals(notificacaoId))
                .findFirst()
                .map(n -> {
                    n.setStatus(StatusNotificacao.LIDA);
                    n.setDataLeitura(LocalDateTime.now());
                    return true;
                })
                .orElse(false);
    }
    
    /**
     * Habilita ou desabilita um canal de notificação
     */
    public void configurarCanal(TipoNotificacao tipo, boolean habilitado) {
        if (tipo != null) {
            canaisHabilitados.put(tipo, habilitado);
        }
    }
    
    /**
     * Verifica se um canal está habilitado
     */
    public boolean isCanalHabilitado(TipoNotificacao tipo) {
        return canaisHabilitados.getOrDefault(tipo, false);
    }
    
    /**
     * Conta notificações não lidas de um usuário
     */
    public long contarNaoLidas(String usuarioId) {
        if (usuarioId == null) {
            return 0;
        }
        
        return notificacoesPorUsuario.getOrDefault(usuarioId, Collections.emptyList())
                .stream()
                .filter(n -> n.getStatus() == StatusNotificacao.ENVIADA)
                .count();
    }
    
    /**
     * Simula envio baseado no tipo
     */
    private boolean simularEnvio(TipoNotificacao tipo) {
        // Simula falha ocasional para alguns tipos
        switch (tipo) {
            case SMS:
                return Math.random() > 0.1; // 10% de chance de falha
            case WHATSAPP:
                return Math.random() > 0.05; // 5% de chance de falha
            default:
                return true;
        }
    }
    
    /**
     * Limpa notificações antigas
     */
    public int limparNotificacoes(String usuarioId, int diasParaManter) {
        if (usuarioId == null || diasParaManter < 0) {
            return 0;
        }
        
        List<Notificacao> notificacoes = notificacoesPorUsuario.get(usuarioId);
        if (notificacoes == null) {
            return 0;
        }
        
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(diasParaManter);
        int tamanhoAnterior = notificacoes.size();
        
        notificacoes.removeIf(n -> n.getDataEnvio().isBefore(dataLimite));
        
        return tamanhoAnterior - notificacoes.size();
    }
    
    public enum TipoNotificacao {
        EMAIL, SMS, PUSH, WHATSAPP
    }
    
    public enum StatusNotificacao {
        ENVIADA, LIDA, ERRO
    }
    
    public static class Notificacao {
        private String id;
        private String usuarioId;
        private TipoNotificacao tipo;
        private String titulo;
        private String mensagem;
        private LocalDateTime dataEnvio;
        private LocalDateTime dataLeitura;
        private StatusNotificacao status;
        
        // Getters e setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getUsuarioId() { return usuarioId; }
        public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
        
        public TipoNotificacao getTipo() { return tipo; }
        public void setTipo(TipoNotificacao tipo) { this.tipo = tipo; }
        
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        
        public String getMensagem() { return mensagem; }
        public void setMensagem(String mensagem) { this.mensagem = mensagem; }
        
        public LocalDateTime getDataEnvio() { return dataEnvio; }
        public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
        
        public LocalDateTime getDataLeitura() { return dataLeitura; }
        public void setDataLeitura(LocalDateTime dataLeitura) { this.dataLeitura = dataLeitura; }
        
        public StatusNotificacao getStatus() { return status; }
        public void setStatus(StatusNotificacao status) { this.status = status; }
    }
}