package br.com.seryoustech.standard.controller;

import br.com.seryoustech.shared.exception.ErrorResponseDTO;
import br.com.seryoustech.standard.dto.ProductRequestDTO;
import br.com.seryoustech.standard.dto.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Interface de contrato REST (MAPI) para o módulo REST Padrão.
 */
@Tag(name = "1. REST Padrão - Produtos", description = "Endpoints da arquitetura REST clássica em camadas para o catálogo de produtos")
@RequestMapping("/api/v1/products")
public interface ProductMAPI {

    @PostMapping
    @Operation(summary = "Cadastrar novo produto", description = "Cria um novo produto com validações de dados obrigatórios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Violação de regra de negócio (ex: nome duplicado)",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO requestDTO);

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna todos os produtos ou filtra apenas os ativos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponseDTO.class))))
    })
    ResponseEntity<List<ProductResponseDTO>> findAll(
            @Parameter(description = "Se true, filtra apenas produtos ativos")
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly
    );

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Localiza os detalhes de um produto a partir de seu identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<ProductResponseDTO> findById(
            @Parameter(description = "Identificador numérico do produto", example = "1")
            @PathVariable Long id
    );

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza todos os dados de um produto existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "422", description = "Regra de negócio violada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<ProductResponseDTO> update(
            @Parameter(description = "Identificador numérico do produto", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO
    );

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status ativo do produto", description = "Ativa ou inativa o produto no catálogo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<ProductResponseDTO> updateStatus(
            @Parameter(description = "Identificador numérico do produto", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novo status (true para ativo, false para inativo)", example = "false")
            @RequestParam boolean active
    );

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir produto", description = "Exclui permanentemente um produto pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "Identificador numérico do produto", example = "1")
            @PathVariable Long id
    );
}
