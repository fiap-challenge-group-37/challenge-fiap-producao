package com.fiap.challenge.cliente.adapters.out.persistence;

import com.fiap.challenge.cliente.domain.entities.Sessao;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sessao")
public class SessaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;
    private String token;
    private Date dataCriacao;

    // Construtor vazio (obrigatório para JPA)
    public SessaoEntity() {}

    // Getters
    public Long getId() {
        return id;
    }
    public String getCpf() {
        return cpf;
    }
    public String getToken() {
        return token;
    }
    public Date getDataCriacao() {
        return dataCriacao;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // Conversor para domínio
    public Sessao toDomain() {
        Sessao sessao = new Sessao();
        sessao.setId(this.id);
        sessao.setCpf(this.cpf);
        sessao.setToken(this.token);
        sessao.setDataCriacao(this.dataCriacao);
        return sessao;
    }

    // Conversor estático de domínio para entity
    public static SessaoEntity fromDomain(Sessao sessao) {
        SessaoEntity entity = new SessaoEntity();
        entity.setId(sessao.getId());
        entity.setCpf(sessao.getCpf());
        entity.setToken(sessao.getToken());
        entity.setDataCriacao(sessao.getDataCriacao());
        return entity;
    }
}