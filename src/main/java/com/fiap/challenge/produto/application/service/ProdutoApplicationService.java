package com.fiap.challenge.produto.application.service;

import com.fiap.challenge.produto.application.exception.ApplicationServiceException;
import com.fiap.challenge.produto.application.port.in.*;
import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import com.fiap.challenge.produto.domain.port.ProdutoRepository;
import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoApplicationService implements
        CriarProdutoUseCase,
        AtualizarProdutoUseCase,
        RemoverProdutoUseCase,
        BuscarProdutoPorIdUseCase,
        BuscarProdutoPorCategoriaUseCase,
        ListarTodosProdutosUseCase {

    private final ProdutoRepository produtoRepository;

    public ProdutoApplicationService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    @Override
    public Produto executar(ProdutoDTO produtoDTO) { // Mantém 'executar' para CriarProdutoUseCase
        Produto produto = produtoDTO.toDomain();
        return produtoRepository.save(produto);
    }

    @Transactional
    @Override
    public Optional<Produto> executar(Long id, ProdutoDTO produtoDTO) { // Mantém 'executar' para AtualizarProdutoUseCase
        Optional<Produto> produtoExistenteOptional = produtoRepository.findById(id);

        if (produtoExistenteOptional.isPresent()) {
            Produto produtoExistente = produtoExistenteOptional.get();
            produtoExistente.setNome(produtoDTO.getNome());
            try {
                produtoExistente.setCategoria(Categoria.fromString(produtoDTO.getCategoria()));
            } catch (IllegalArgumentException e) {
                throw new ApplicationServiceException("Categoria inválida fornecida: " + produtoDTO.getCategoria(), e);
            }
            produtoExistente.setPreco(produtoDTO.getPreco());
            produtoExistente.setDescricao(produtoDTO.getDescricao());
            return Optional.of(produtoRepository.save(produtoExistente));
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public boolean removerPorId(Long id) { // Implementa o método renomeado de RemoverProdutoUseCase
        if (produtoRepository.findById(id).isPresent()) {
            produtoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Produto> buscarPorId(Long id) { // Implementa o método renomeado de BuscarProdutoPorIdUseCase
        return produtoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Produto> executar(Categoria categoria) { // Mantém 'executar' para BuscarProdutoPorCategoriaUseCase
        return produtoRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Produto> executar() { // Mantém 'executar' para ListarTodosProdutosUseCase
        return produtoRepository.findAll();
    }
}