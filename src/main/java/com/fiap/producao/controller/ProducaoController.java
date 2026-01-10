package com.fiap.producao.controller;

import com.fiap.producao.domain.dto.StatusDTO;
import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.integration.PedidoIntegrationService;
import com.fiap.producao.repository.PedidoProducaoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/producao") // Rota base "bonitinha"
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
                .sorted() // Usa o compareTo da entidade (Pronto > Em Prep > Recebido > Antigo)
                .map(PedidoDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Atualiza status do pedido na produção e notifica pedidos")
    @PatchMapping("/{id}/status") // Endpoint final: PATCH /producao/{id}/status
    public ResponseEntity<PedidoDTO> atualizarStatus(@PathVariable String id, @RequestBody @Valid StatusDTO statusDTO) {
        return repository.findById(id)
                .map(pedido -> {
                    // 1. Atualiza Local (DynamoDB)
                    pedido.setStatus(statusDTO.status());
                    pedido.setDataAtualizacao(java.time.LocalDateTime.now());
                    repository.save(pedido);

                    // 2. Chama Integração (Avisa MS Pedidos com Token)
                    pedidoIntegrationService.atualizarStatusNoMsPedidos(
                            pedido.getIdPedidoOriginal(),
                            statusDTO.status()
                    );

                    return ResponseEntity.ok(PedidoDTO.fromEntity(pedido));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}