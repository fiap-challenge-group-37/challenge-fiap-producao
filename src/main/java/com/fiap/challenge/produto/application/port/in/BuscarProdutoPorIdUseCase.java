package com.fiap.challenge.produto.application.port.in;

import com.fiap.challenge.produto.domain.entities.Produto;
import java.util.Optional;

public interface BuscarProdutoPorIdUseCase {
    Optional<Produto> buscarPorId(Long id); // Renomeado de executar para buscarPorId
}