package com.fiap.challenge.produto.adapters.in.http;

import com.fiap.challenge.produto.adapters.in.http.dto.ProdutoDTO;
import com.fiap.challenge.produto.application.exception.ApplicationServiceException;
import com.fiap.challenge.produto.application.port.in.*;
import com.fiap.challenge.produto.domain.entities.Categoria;
import com.fiap.challenge.produto.domain.entities.Produto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produto Controller", description = "Operações CRUD para gerenciamento de produtos")
public class ProdutoController {

    private final CriarProdutoUseCase criarProdutoUseCase;
    private final AtualizarProdutoUseCase atualizarProdutoUseCase;
    private final RemoverProdutoUseCase removerProdutoUseCase;
    private final BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase;
    private final BuscarProdutoPorCategoriaUseCase buscarProdutoPorCategoriaUseCase;
    private final ListarTodosProdutosUseCase listarTodosProdutosUseCase;

    public ProdutoController(CriarProdutoUseCase criarProdutoUseCase,
                             AtualizarProdutoUseCase atualizarProdutoUseCase,
                             RemoverProdutoUseCase removerProdutoUseCase,
                             BuscarProdutoPorIdUseCase buscarProdutoPorIdUseCase,
                             BuscarProdutoPorCategoriaUseCase buscarProdutoPorCategoriaUseCase,
                             ListarTodosProdutosUseCase listarTodosProdutosUseCase) {
        this.criarProdutoUseCase = criarProdutoUseCase;
        this.atualizarProdutoUseCase = atualizarProdutoUseCase;
        this.removerProdutoUseCase = removerProdutoUseCase;
        this.buscarProdutoPorIdUseCase = buscarProdutoPorIdUseCase;
        this.buscarProdutoPorCategoriaUseCase = buscarProdutoPorCategoriaUseCase;
        this.listarTodosProdutosUseCase = listarTodosProdutosUseCase;
    }

    @Operation(summary = "Cadastrar novo produto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para o produto")
    })
    @PostMapping
    public ResponseEntity<ProdutoDTO> criarProduto(@Valid @RequestBody ProdutoDTO dto) {
        Produto novoProduto = criarProdutoUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoDTO.fromDomain(novoProduto));
    }

    @Operation(summary = "Editar produto existente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização ou categoria inválida"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{produto_id}")
    public ResponseEntity<ProdutoDTO> editarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        Optional<Produto> produtoAtualizadoOptional = atualizarProdutoUseCase.executar(id, dto);
        return produtoAtualizadoOptional
                .map(produto -> ResponseEntity.ok(ProdutoDTO.fromDomain(produto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Remover produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{produto_id}")
    public ResponseEntity<Void> removerProduto(@PathVariable Long id) {
        boolean removido = removerProdutoUseCase.removerPorId(id); // Chamada ao método renomeado
        if (removido) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar produtos por categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados"),
            @ApiResponse(responseCode = "400", description = "Categoria inválida")
    })
    @GetMapping("/categoria/{categoriaNome}")
    public ResponseEntity<List<ProdutoDTO>> buscarPorCategoria(@PathVariable String categoriaNome) {
        Categoria categoriaEnum = Categoria.fromString(categoriaNome.toUpperCase());
        List<Produto> produtos = buscarProdutoPorCategoriaUseCase.executar(categoriaEnum);
        List<ProdutoDTO> dtos = produtos.stream()
                .map(ProdutoDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Listar todos os produtos cadastrados")
    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodosProdutos() {
        List<Produto> produtos = listarTodosProdutosUseCase.executar();
        List<ProdutoDTO> dtos = produtos.stream()
                .map(ProdutoDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Buscar produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{produto_id}")
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable Long id) {
        return buscarProdutoPorIdUseCase.buscarPorId(id) // Chamada ao método renomeado
                .map(produto -> ResponseEntity.ok(ProdutoDTO.fromDomain(produto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Exception Handlers

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("erro", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ApplicationServiceException.class)
    public ResponseEntity<Map<String, String>> handleApplicationServiceException(ApplicationServiceException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("erro", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        // É uma boa prática logar a exceção aqui
        // logger.error("Erro inesperado na aplicação: ", ex);
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("erro", "Ocorreu um erro interno inesperado. Tente novamente mais tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}