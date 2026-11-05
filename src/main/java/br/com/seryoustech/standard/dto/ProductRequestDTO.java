package br.com.seryoustech.standard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Record DTO para dados de entrada no REST Padrão.
 */
@Schema(description = "Dados para cadastro ou atualização de um produto")
public record ProductRequestDTO(

        @Schema(description = "Nome comercial do produto", example = "Teclado Mecânico RGB")
        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve conter entre 2 e 100 caracteres")
        String name,

        @Schema(description = "Descrição detalhada do produto", example = "Teclado mecânico switch brown padrão ABNT2")
        @Size(max = 255, message = "A descrição não pode ultrapassar 255 caracteres")
        String description,

        @Schema(description = "Preço unitário do produto", example = "349.90")
        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser estritamente maior que zero")
        BigDecimal price,

        @Schema(description = "Quantidade disponível em estoque", example = "25")
        @NotNull(message = "A quantidade em estoque é obrigatória")
        @PositiveOrZero(message = "A quantidade em estoque não pode ser negativa")
        Integer stockQuantity,

        @Schema(description = "Flag de produto ativo no catálogo", example = "true", defaultValue = "true")
        Boolean active
) {
}
