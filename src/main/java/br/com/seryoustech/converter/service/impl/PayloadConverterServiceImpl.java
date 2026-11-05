package br.com.seryoustech.converter.service.impl;

import br.com.seryoustech.converter.model.ProductPayload;
import br.com.seryoustech.converter.port.in.ConvertPayloadUseCase;
import br.com.seryoustech.converter.port.out.XmlTransformationPort;
import br.com.seryoustech.shared.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Caso de uso do core da aplicação implementando ConvertPayloadUseCase (Arquitetura Hexagonal).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayloadConverterServiceImpl implements ConvertPayloadUseCase {

    private final XmlTransformationPort xmlTransformationPort;
    private final ObjectMapper objectMapper;

    @Override
    public String convertJsonToXml(String jsonContent) {
        log.info("[REST Conversor] Processando conversão de payload JSON para XML");

        if (jsonContent == null || jsonContent.isBlank()) {
            throw new BusinessException("O conteúdo JSON não pode estar vazio");
        }

        try {
            if (jsonContent.contains("codigoProduto") || jsonContent.contains("nomeProduto")) {
                ProductPayload product = objectMapper.readValue(jsonContent, ProductPayload.class);
                return xmlTransformationPort.transformToXml(product);
            }

            JsonNode rootNode = objectMapper.readTree(jsonContent);
            Map<String, Object> fieldsMap = new LinkedHashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                String targetXmlTag = resolveXmlTag(key);
                JsonNode value = field.getValue();
                fieldsMap.put(targetXmlTag, value.isTextual() ? value.asText() : value.toString());
            }

            return xmlTransformationPort.transformMapToXml("PRODUTO", fieldsMap);

        } catch (IOException e) {
            log.error("[REST Conversor] Erro ao realizar parse do JSON para conversão XML", e);
            throw new BusinessException("Formato JSON inválido: " + e.getMessage(), e);
        }
    }

    @Override
    public String convertJsonFileToXml(byte[] fileBytes) {
        if (fileBytes == null || fileBytes.length == 0) {
            throw new BusinessException("O arquivo JSON enviado está vazio");
        }
        String jsonContent = new String(fileBytes, StandardCharsets.UTF_8);
        return convertJsonToXml(jsonContent);
    }

    @Override
    public String convertObjectToXml(Object model) {
        return xmlTransformationPort.transformToXml(model);
    }

    private String resolveXmlTag(String jsonKey) {
        return switch (jsonKey) {
            case "codigoProduto", "idProduto", "codigo" -> "CODPROD";
            case "nomeProduto", "nome", "descricao" -> "NOMEPROD";
            case "descricaoProduto", "detalhes" -> "DESCRICAOPROD";
            case "valorPreco", "preco", "valorUnitario" -> "VLRPRECO";
            case "quantidadeEstoque", "quantidade", "estoque" -> "QTDESTOQUE";
            case "statusAtivo", "ativo" -> "STATUSATIVO";
            default -> jsonKey.toUpperCase();
        };
    }
}
