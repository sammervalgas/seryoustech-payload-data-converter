package br.com.seryoustech.converter.adapter.in.web;

import br.com.seryoustech.converter.annotation.AutoConvertToXml;
import br.com.seryoustech.converter.model.ProductPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface de contrato REST (MAPI) para a estrutura do Conversor.
 */
@Tag(name = "2. REST Conversor - Ports & Adapters", description = "Endpoints da arquitetura hexagonal (Port & Adapter) para conversão JSON/XML")
@RequestMapping("/api/v1/converter")
public interface ConverterMAPI {

    @PostMapping(value = "/json-to-xml", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(
            summary = "Converter JSON direto para XML",
            description = "Recebe um payload JSON (ex: codigoProduto, nomeProduto) e retorna XML com tags convertidas (ex: <CODPROD>)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "XML convertido com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(example = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<PRODUTO>\n  <CODPROD>PRD-001</CODPROD>\n</PRODUTO>"))),
            @ApiResponse(responseCode = "400", description = "JSON inválido ou malformado")
    })
    ResponseEntity<String> convertJsonToXml(
            @Parameter(description = "JSON com os dados para conversão", required = true)
            @RequestBody String jsonContent
    );

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(
            summary = "Converter arquivo JSON via Upload para XML",
            description = "Recebe um arquivo .json via multipart/form-data e gera a saída XML convertida"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo processado e XML gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou vazio")
    })
    ResponseEntity<String> convertJsonFile(
            @Parameter(description = "Arquivo JSON a ser convertido", required = true)
            @RequestParam("file") MultipartFile file
    );

    @PostMapping(value = "/aspect-conversion", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(
            summary = "Conversão automática transparente via Aspecto AOP",
            description = "Recebe o objeto ProductPayload no controller. O método retorna o próprio objeto Java anotado, "
                    + "e o Aspecto (@AutoConvertToXml) intercepta e converte automaticamente a saída para XML com aliases."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "XML gerado automaticamente via interceptação por Aspecto AOP")
    })
    @AutoConvertToXml
    ResponseEntity<Object> convertViaAspect(
            @Parameter(description = "Objeto de domínio com anotações @XmlFieldAlias", required = true)
            @RequestBody ProductPayload payload
    );
}
