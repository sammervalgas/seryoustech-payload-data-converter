package br.com.seryoustech.converter.port.out;

import java.util.Map;

/**
 * Porta de Saída (Driven / Outbound Port):
 * Contrato de infraestrutura para serialização de dados em formato XML.
 */
public interface XmlTransformationPort {

    String transformToXml(Object model);

    String transformMapToXml(String rootName, Map<String, Object> fields);
}
