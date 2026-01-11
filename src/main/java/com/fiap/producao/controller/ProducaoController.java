package com.fiap.producao.controller;

import com.fiap.producao.domain.dto.PedidoDTO;
import com.fiap.producao.domain.dto.StatusDTO;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.integration.PedidoIntegrationService;
import com.fiap.producao.repository.PedidoProducaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/producao")
@RequiredArgsConstructor
@Tag(name = "Produção", description = "Gerenciamento da fila de cozinha")
public class ProducaoController {

    private final PedidoProducaoRepository repository;
    private final PedidoIntegrationService pedidoIntegrationService;

    @Operation(summary = "Lista pedidos na fila (ordenados)")
    @GetMapping("/fila")
    public ResponseEntity<List<PedidoDTO>> listarFila() {
        List<PedidoProducao> pedidos = repository.findAll();

        List<PedidoDTO> dtos = pedidos.stream()
                .sorted(Comparator
                        .comparingInt((PedidoProducao p) -> prioridadeStatus(p.getStatus()))
                        .thenComparing(PedidoProducao::getDataEntrada,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                )
                .map(PedidoDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoDTO> atualizarStatus(@PathVariable String id,
                                                     @RequestBody @Valid StatusDTO statusDTO) {
        return repository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusDTO.status());
                    pedido.setDataAtualizacao(LocalDateTime.now());
                    repository.save(pedido);

                    pedidoIntegrationService.atualizarStatusNoMsPedidos(
                            pedido.getIdPedidoOriginal(),
                            statusDTO.status()
                    );

                    return ResponseEntity.ok(PedidoDTO.fromEntity(pedido));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Ordem da fila:
     * PRONTO -> EM_PREPARACAO -> RECEBIDO -> FINALIZADO
     */
    private int prioridadeStatus(StatusPedido status) {
        if (status == null) return 999;

        return switch (status) {
            case PRONTO -> 1;
            case EM_PREPARACAO -> 2;
            case RECEBIDO -> 3;
            case FINALIZADO -> 4;
        };
    }
}
