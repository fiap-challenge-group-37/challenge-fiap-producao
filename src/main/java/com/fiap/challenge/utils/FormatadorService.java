package com.fiap.challenge.utils;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Serviço para formatação de dados
 */
@Service
public class FormatadorService {
    
    private static final DateTimeFormatter FORMATO_BRASILEIRO = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    private static final DateTimeFormatter FORMATO_ISO = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    
    /**
     * Formata CPF com máscara
     */
    public String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos numéricos");
        }
        
        return String.format("%s.%s.%s-%s", 
            cpf.substring(0, 3),
            cpf.substring(3, 6), 
            cpf.substring(6, 9),
            cpf.substring(9, 11));
    }
    
    /**
     * Remove formatação do CPF
     */
    public String limparCpf(String cpfFormatado) {
        if (cpfFormatado == null) {
            return null;
        }
        
        return cpfFormatado.replaceAll("[^0-9]", "");
    }
    
    /**
     * Formata CNPJ com máscara
     */
    public String formatarCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d{14}")) {
            throw new IllegalArgumentException("CNPJ deve conter exatamente 14 dígitos numéricos");
        }
        
        return String.format("%s.%s.%s/%s-%s",
            cnpj.substring(0, 2),
            cnpj.substring(2, 5),
            cnpj.substring(5, 8),
            cnpj.substring(8, 12),
            cnpj.substring(12, 14));
    }
    
    /**
     * Formata telefone com máscara
     */
    public String formatarTelefone(String telefone) {
        if (telefone == null) {
            return null;
        }
        
        String numeroLimpo = telefone.replaceAll("[^0-9]", "");
        
        if (numeroLimpo.length() == 10) {
            return String.format("(%s) %s-%s",
                numeroLimpo.substring(0, 2),
                numeroLimpo.substring(2, 6),
                numeroLimpo.substring(6, 10));
        } else if (numeroLimpo.length() == 11) {
            return String.format("(%s) %s-%s",
                numeroLimpo.substring(0, 2),
                numeroLimpo.substring(2, 7),
                numeroLimpo.substring(7, 11));
        }
        
        return telefone;
    }
    
    /**
     * Formata data e hora no padrão brasileiro
     */
    public String formatarDataHoraBrasileira(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        return dateTime.format(FORMATO_BRASILEIRO);
    }
    
    /**
     * Formata data e hora no padrão ISO
     */
    public String formatarDataHoraISO(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        
        return dateTime.format(FORMATO_ISO);
    }
    
    /**
     * Converte string para formato título (primeira letra maiúscula)
     */
    public String formatarTitulo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return texto;
        }
        
        String textoLimpo = texto.trim().toLowerCase();
        String[] palavras = textoLimpo.split("\\s+");
        StringBuilder resultado = new StringBuilder();
        
        for (int i = 0; i < palavras.length; i++) {
            if (i > 0) {
                resultado.append(" ");
            }
            
            String palavra = palavras[i];
            if (!palavra.isEmpty()) {
                resultado.append(Character.toUpperCase(palavra.charAt(0)));
                if (palavra.length() > 1) {
                    resultado.append(palavra.substring(1));
                }
            }
        }
        
        return resultado.toString();
    }
    
    /**
     * Mascara informações sensíveis (mostra apenas primeiros e últimos caracteres)
     */
    public String mascararInformacao(String informacao, int caracteresVisiveis) {
        if (informacao == null || informacao.length() <= caracteresVisiveis * 2) {
            return informacao;
        }
        
        String inicio = informacao.substring(0, caracteresVisiveis);
        String fim = informacao.substring(informacao.length() - caracteresVisiveis);
        int caracteresOcultos = informacao.length() - (caracteresVisiveis * 2);
        
        return inicio + "*".repeat(caracteresOcultos) + fim;
    }
}