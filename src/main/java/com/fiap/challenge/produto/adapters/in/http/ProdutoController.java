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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    // Definição do Logger
    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);

    private static final String ERRO_INESPERADO_MSG = "Ocorreu um erro interno inesperado. Tente novamente mais tarde.";
    private static final String ERRO_KEY = "erro";


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
            @ApiResponse(responseCode = "400", description = "Dados inválidos para o produto"),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados")
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
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "422", description = "Erro de validação nos dados enviados")
    })
    @PutMapping("/{produto_id}") // Nome da variável no path
    public ResponseEntity<ProdutoDTO> editarProduto(@PathVariable("produto_id") Long produtoId, @Valid @RequestBody ProdutoDTO dto) { // Nome do parâmetro correspondente
        Optional<Produto> produtoAtualizadoOptional = atualizarProdutoUseCase.executar(produtoId, dto);
        return produtoAtualizadoOptional
                .map(produto -> ResponseEntity.ok(ProdutoDTO.fromDomain(produto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Remover produto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{produto_id}") // Nome da variável no path
    public ResponseEntity<Void> removerProduto(@PathVariable("produto_id") Long produtoId) { // Nome do parâmetro correspondente
        boolean removido = removerProdutoUseCase.removerPorId(produtoId);
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
        // A conversão para Enum pode lançar IllegalArgumentException, que será tratada pelo ExceptionHandler
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
    @GetMapping("/{produto_id}") // Nome da variável no path
    public ResponseEntity<ProdutoDTO> buscarProdutoPorId(@PathVariable("produto_id") Long produtoId) { // Nome do parâmetro correspondente
        return buscarProdutoPorIdUseCase.buscarPorId(produtoId)
                .map(produto -> ResponseEntity.ok(ProdutoDTO.fromDomain(produto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Exception Handlers

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // Sonar pode sugerir status mais específico para validação
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.warn("Erro de validação: {}", errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERRO_KEY, ex.getMessage());
        logger.warn("Argumento ilegal: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ApplicationServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Ou outro status dependendo da natureza da exceção
    public ResponseEntity<Map<String, String>> handleApplicationServiceException(ApplicationServiceException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERRO_KEY, ex.getMessage());
        logger.error("Erro na camada de aplicação: {}", ex.getMessage(), ex); // Logar a exceção completa
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("Erro inesperado na aplicação: {}", ex.getMessage(), ex); // Logar a exceção completa
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERRO_KEY, ERRO_INESPERADO_MSG);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
