package br.com.seryoustech.shared.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Record DTO para padronização de respostas de erro da API.
 */
@Schema(description = "Estrutura padrão de resposta para erros da API")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(

        @Schema(description = "Timestamp da ocorrência do erro", example = "2026-11-05T10:45:00")
        LocalDateTime timestamp,

        @Schema(description = "Código HTTP de status", example = "404")
        Integer status,

        @Schema(description = "Classificação breve do erro", example = "Recurso Não Encontrado")
        String error,

        @Schema(description = "Mensagem explicativa e amigável sobre o erro", example = "Produto não encontrado(a) com o identificador: 99")
        String message,

        @Schema(description = "URI da requisição que originou o erro", example = "/api/v1/products/99")
        String path,

        @Schema(description = "Mapa detalhado de erros por campo", example = "{\"price\": \"O preço deve ser estritamente maior que zero\"}")
        Map<String, String> fieldErrors
) {
    public static ErrorResponseDTO of(Integer status, String error, String message, String path) {
        return new ErrorResponseDTO(LocalDateTime.now(), status, error, message, path, null);
    }

    public static ErrorResponseDTO withFieldErrors(Integer status, String error, String message, String path, Map<String, String> fieldErrors) {
        return new ErrorResponseDTO(LocalDateTime.now(), status, error, message, path, fieldErrors);
    }
}
