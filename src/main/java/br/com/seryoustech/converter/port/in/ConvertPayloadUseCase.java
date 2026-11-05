package br.com.seryoustech.converter.port.in;

/**
 * Porta de Entrada (Driving / Inbound Port):
 * Caso de uso do conversor de payload JSON para XML.
 */
public interface ConvertPayloadUseCase {

    String convertJsonToXml(String jsonContent);

    String convertJsonFileToXml(byte[] fileBytes);

    String convertObjectToXml(Object model);
}
