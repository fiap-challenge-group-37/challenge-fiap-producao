package com.fiap.challenge.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Serviço para validações de dados
 */
@Service
public class ValidadorService {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern TELEFONE_PATTERN = 
        Pattern.compile("^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$");
    
    /**
     * Valida formato de email
     */
    public boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Valida formato de telefone brasileiro
     */
    public boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        
        return TELEFONE_PATTERN.matcher(telefone.trim()).matches();
    }
    
    /**
     * Valida se uma string tem o tamanho mínimo
     */
    public boolean validarTamanhoMinimo(String texto, int tamanhoMinimo) {
        if (texto == null) {
            return false;
        }
        
        return texto.length() >= tamanhoMinimo;
    }
    
    /**
     * Valida se uma string está dentro do range de tamanho
     */
    public boolean validarTamanhoRange(String texto, int minimo, int maximo) {
        if (texto == null) {
            return false;
        }
        
        int tamanho = texto.length();
        return tamanho >= minimo && tamanho <= maximo;
    }
    
    /**
     * Valida se um número está dentro do range
     */
    public boolean validarNumeroRange(Integer numero, int minimo, int maximo) {
        if (numero == null) {
            return false;
        }
        
        return numero >= minimo && numero <= maximo;
    }
    
    /**
     * Valida se uma string contém apenas números
     */
    public boolean validarApenasNumeros(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        
        return texto.trim().matches("\\d+");
    }
    
    /**
     * Valida força da senha
     */
    public TipoForcaSenha validarForcaSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            return TipoForcaSenha.INVALIDA;
        }
        
        boolean temMinuscula = senha.matches(".*[a-z].*");
        boolean temMaiuscula = senha.matches(".*[A-Z].*");
        boolean temNumero = senha.matches(".*\\d.*");
        boolean temEspecial = senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        int pontos = 0;
        if (senha.length() >= 8) pontos++;
        if (temMinuscula) pontos++;
        if (temMaiuscula) pontos++;
        if (temNumero) pontos++;
        if (temEspecial) pontos++;
        
        if (pontos <= 2) return TipoForcaSenha.FRACA;
        if (pontos <= 3) return TipoForcaSenha.MEDIA;
        return TipoForcaSenha.FORTE;
    }
    
    /**
     * Enum para classificar força da senha
     */
    public enum TipoForcaSenha {
        INVALIDA, FRACA, MEDIA, FORTE
    }
}