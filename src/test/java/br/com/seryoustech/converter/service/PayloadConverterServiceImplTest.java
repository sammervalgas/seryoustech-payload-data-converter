package br.com.seryoustech.converter.service;

import br.com.seryoustech.converter.adapter.out.xml.JacksonXmlTransformationAdapter;
import br.com.seryoustech.converter.service.impl.PayloadConverterServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PayloadConverterServiceImplTest {

    private PayloadConverterServiceImpl payloadConverterService;

    @BeforeEach
    void setUp() {
        JacksonXmlTransformationAdapter xmlAdapter = new JacksonXmlTransformationAdapter();
        ObjectMapper objectMapper = new ObjectMapper();
        payloadConverterService = new PayloadConverterServiceImpl(xmlAdapter, objectMapper);
    }

    @Test
    @DisplayName("Deve converter string JSON em XML com mapeamento de aliases de campos")
    void shouldConvertJsonStringToXmlSuccessfully() {
        String json = """
                {
                    "codigoProduto": "XYZ-99",
                    "nomeProduto": "Mouse Gamer",
                    "descricaoProduto": "Mouse RGB",
                    "valorPreco": 150.00,
                    "quantidadeEstoque": 30,
                    "statusAtivo": true
                }
                """;

        String xml = payloadConverterService.convertJsonToXml(json);

        assertNotNull(xml);
        assertTrue(xml.contains("<PRODUTO>"));
        assertTrue(xml.contains("<CODPROD>XYZ-99</CODPROD>"));
        assertTrue(xml.contains("<NOMEPROD>Mouse Gamer</NOMEPROD>"));
        assertTrue(xml.contains("<VLRPRECO>150.00</VLRPRECO>"));
    }

    @Test
    @DisplayName("Deve converter arquivo JSON simulado em bytes para XML")
    void shouldConvertJsonFileBytesToXml() {
        String json = """
                {
                    "codigoProduto": "FILE-01",
                    "nomeProduto": "Teclado",
                    "valorPreco": 300.00,
                    "quantidadeEstoque": 5,
                    "statusAtivo": true
                }
                """;

        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        String xml = payloadConverterService.convertJsonFileToXml(bytes);

        assertNotNull(xml);
        assertTrue(xml.contains("<CODPROD>FILE-01</CODPROD>"));
        assertTrue(xml.contains("<NOMEPROD>Teclado</NOMEPROD>"));
    }
}
