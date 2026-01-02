package com.fiap.producao.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemProducao {
    private String nome;
    private Integer quantidade;
}
