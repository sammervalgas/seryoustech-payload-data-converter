package br.com.seryoustech.converter.adapter.out.xml;

import br.com.seryoustech.converter.model.ProductPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JacksonXmlTransformationAdapterTest {

    private JacksonXmlTransformationAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new JacksonXmlTransformationAdapter();
    }

    @Test
    @DisplayName("Deve converter ProductPayload para XML utilizando os aliases anotados (@XmlFieldAlias e @XmlRootAlias)")
    void shouldTransformProductPayloadToXmlWithAliases() {
        ProductPayload payload = new ProductPayload(
                "PRD-2026-X1",
                "Monitor Ultrawide 29",
                "Monitor IPS 75Hz Full HD",
                new BigDecimal("1299.00"),
                50,
                true
        );

        String xml = adapter.transformToXml(payload);

        assertNotNull(xml);
        assertTrue(xml.contains("<PRODUTO>"), "Deveria conter a tag raiz <PRODUTO>");
        assertTrue(xml.contains("</PRODUTO>"), "Deveria fechar a tag raiz </PRODUTO>");
        assertTrue(xml.contains("<CODPROD>PRD-2026-X1</CODPROD>"), "Deveria mapear codigoProduto para CODPROD");
        assertTrue(xml.contains("<NOMEPROD>Monitor Ultrawide 29</NOMEPROD>"), "Deveria mapear nomeProduto para NOMEPROD");
        assertTrue(xml.contains("<VLRPRECO>1299.00</VLRPRECO>"), "Deveria mapear valorPreco para VLRPRECO");
        assertTrue(xml.contains("<QTDESTOQUE>50</QTDESTOQUE>"), "Deveria mapear quantidadeEstoque para QTDESTOQUE");
        assertTrue(xml.contains("<STATUSATIVO>true</STATUSATIVO>"), "Deveria mapear statusAtivo para STATUSATIVO");
    }
}
