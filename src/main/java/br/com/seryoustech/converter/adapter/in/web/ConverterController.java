package br.com.seryoustech.converter.adapter.in.web;

import br.com.seryoustech.converter.annotation.AutoConvertToXml;
import br.com.seryoustech.converter.model.ProductPayload;
import br.com.seryoustech.converter.port.in.ConvertPayloadUseCase;
import br.com.seryoustech.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller da estrutura REST do Conversor (Driving Adapter).
 * Implementa o contrato ConverterMAPI.
 */
@RestController
@RequiredArgsConstructor
public class ConverterController implements ConverterMAPI {

    private final ConvertPayloadUseCase convertPayloadUseCase;

    @Override
    public ResponseEntity<String> convertJsonToXml(String jsonContent) {
        String xml = convertPayloadUseCase.convertJsonToXml(jsonContent);
        return ResponseEntity.ok(xml);
    }

    @Override
    public ResponseEntity<String> convertJsonFile(MultipartFile file) {
        try {
            String xml = convertPayloadUseCase.convertJsonFileToXml(file.getBytes());
            return ResponseEntity.ok(xml);
        } catch (IOException e) {
            throw new BusinessException("Erro ao ler o arquivo JSON recebido: " + e.getMessage(), e);
        }
    }

    @Override
    @AutoConvertToXml
    public ResponseEntity<Object> convertViaAspect(ProductPayload payload) {
        return ResponseEntity.ok(payload);
    }
}
