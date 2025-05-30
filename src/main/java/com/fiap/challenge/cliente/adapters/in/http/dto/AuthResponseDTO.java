package com.fiap.challenge.cliente.adapters.in.http.dto;

import lombok.Getter;

@Getter
public class AuthResponseDTO {
    private String token;

    public AuthResponseDTO(String token) { this.token = token; }
}
