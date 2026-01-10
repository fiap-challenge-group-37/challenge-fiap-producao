package com.fiap.producao.domain.dto;

import com.fiap.producao.domain.entity.ItemProducao;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {
    private String id;
    private Long idPedidoOriginal;
    private List<ItemProducao> itens;
    private StatusPedido status;
    private LocalDateTime dataEntrada;

    public static PedidoDTO fromEntity(PedidoProducao entity) {
        return com.fiap.producao.domain.dto.PedidoDTO.builder()
                .id(entity.getId())
                .idPedidoOriginal(entity.getIdPedidoOriginal())
                .itens(entity.getItens())
                .status(entity.getStatus())
                .dataEntrada(entity.getDataEntrada())
                .build();
    }
}