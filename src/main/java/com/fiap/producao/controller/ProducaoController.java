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
    private final PedidoProducaoRepository repository;

    public ProducaoController(PedidoService service, PedidoProducaoRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping("/fila")
    public ResponseEntity<List<PedidoProducao>> listarFila() {
        return ResponseEntity.ok(service.listarFilaCozinha());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoProducao> atualizarStatus(@PathVariable String id, @RequestBody StatusDTO statusDTO) {
        return repository.findById(id)
                .map(pedido -> {
                    pedido.setStatus(statusDTO.status());
                    pedido.setDataAtualizacao(java.time.LocalDateTime.now());
                    repository.save(pedido);
                    return ResponseEntity.ok(pedido);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

record StatusDTO(StatusPedido status) {}