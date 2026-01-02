package com.fiap.producao.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pedidos_cozinha")
public class PedidoProducao {

    @Id
    private String id;
    private Long idPedidoOriginal;
    private List<ItemProducao> itens;
    private StatusPedido status; 
    private LocalDateTime dataEntrada;
    private LocalDateTime dataAtualizacao;
}
