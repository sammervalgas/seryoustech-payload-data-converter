package br.com.seryoustech.standard.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record DTO para dados de saída no REST Padrão.
 */
@Schema(description = "Representação detalhada de um produto retornado pela API")
public record ProductResponseDTO(

        @Schema(description = "Identificador único do produto", example = "1")
        Long id,

        @Schema(description = "Nome do produto", example = "Teclado Mecânico RGB")
        String name,

        @Schema(description = "Descrição detalhada", example = "Teclado mecânico switch brown padrão ABNT2")
        String description,

        @Schema(description = "Preço unitário", example = "349.90")
        BigDecimal price,

        @Schema(description = "Quantidade em estoque", example = "25")
        Integer stockQuantity,

        @Schema(description = "Indica se o produto está ativo", example = "true")
        Boolean active,

        @Schema(description = "Data e hora de criação", example = "2026-11-05T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Data e hora da última atualização", example = "2026-11-05T10:30:00")
        LocalDateTime updatedAt
) {
}
