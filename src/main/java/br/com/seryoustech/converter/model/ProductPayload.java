package br.com.seryoustech.converter.model;

import br.com.seryoustech.converter.annotation.XmlFieldAlias;
import br.com.seryoustech.converter.annotation.XmlRootAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Modelo de payload representando os dados recebidos via JSON para conversão em XML.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootAlias("PRODUTO")
@Schema(description = "Payload JSON de entrada para conversão com mapeamento de aliases XML")
public class ProductPayload {

    @XmlFieldAlias("CODPROD")
    @Schema(description = "Código identificador do produto", example = "PRD-2026-X1")
    private String codigoProduto;

    @XmlFieldAlias("NOMEPROD")
    @Schema(description = "Nome do produto", example = "Monitor Ultrawide 29")
    private String nomeProduto;

    @XmlFieldAlias("DESCRICAOPROD")
    @Schema(description = "Descrição detalhada do produto", example = "Monitor IPS 75Hz Full HD")
    private String descricaoProduto;

    @XmlFieldAlias("VLRPRECO")
    @Schema(description = "Valor/Preço unitário", example = "1299.00")
    private BigDecimal valorPreco;

    @XmlFieldAlias("QTDESTOQUE")
    @Schema(description = "Quantidade disponível em estoque", example = "50")
    private Integer quantidadeEstoque;

    @XmlFieldAlias("STATUSATIVO")
    @Schema(description = "Status de ativação", example = "true")
    private Boolean statusAtivo;
}
