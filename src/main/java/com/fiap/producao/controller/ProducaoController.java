package com.fiap.producao.controller;

import com.fiap.producao.domain.entity.PedidoProducao;
import com.fiap.producao.domain.entity.StatusPedido;
import com.fiap.producao.service.PedidoService;
import com.fiap.producao.repository.PedidoProducaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producao")
public class ProducaoController {

    private final PedidoService service;
    private final PedidoProducaoRepository repository; // Usando direto aqui pra simplificar update, mas ideal é passar pro service

    public ProducaoController(PedidoService service, PedidoProducaoRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    // Endpoint principal: Lista a fila ordenada para a TV da Cozinha
    @GetMapping("/fila")
    public ResponseEntity<List<PedidoProducao>> listarFila() {
        return ResponseEntity.ok(service.listarFilaCozinha());
    }

    // Endpoint para o Cozinheiro atualizar o status (Pronto, Finalizado)
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoProducao> atualizarStatus(@PathVariable String id, @RequestBody StatusDTO statusDTO) {
        return repository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusDTO.status());
                    pedido.setDataAtualizacao(java.time.LocalDateTime.now());
                    repository.save(pedido);
                    // TODO: Aqui você enviaria um evento para notificar o cliente (via SQS/SNS)
                    return ResponseEntity.ok(pedido);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

// DTO simples para receber o status (record do Java 21)
record StatusDTO(StatusPedido status) {}
