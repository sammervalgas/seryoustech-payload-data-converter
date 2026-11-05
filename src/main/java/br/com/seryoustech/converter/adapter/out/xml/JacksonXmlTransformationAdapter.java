package br.com.seryoustech.converter.adapter.out.xml;

import br.com.seryoustech.converter.annotation.XmlFieldAlias;
import br.com.seryoustech.converter.annotation.XmlRootAlias;
import br.com.seryoustech.converter.port.out.XmlTransformationPort;
import br.com.seryoustech.shared.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Adaptador de Saída (Driven / Outbound Adapter):
 * Serializa objetos e mapas para XML com base nas anotações de alias.
 */
@Component
public class JacksonXmlTransformationAdapter implements XmlTransformationPort {

    @Override
    public String transformToXml(Object model) {
        if (model == null) {
            throw new BusinessException("O objeto para transformação em XML não pode ser nulo");
        }

        Class<?> clazz = model.getClass();
        String rootName = getRootName(clazz);
        Map<String, Object> mappedFields = extractMappedFields(model, clazz);

        return transformMapToXml(rootName, mappedFields);
    }

    @Override
    public String transformMapToXml(String rootName, Map<String, Object> fields) {
        if (rootName == null || rootName.isBlank()) {
            rootName = "ROOT";
        }

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<").append(rootName).append(">\n");

        if (fields != null) {
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                String tagName = entry.getKey();
                Object value = entry.getValue();

                xml.append("  <").append(tagName).append(">");
                if (value != null) {
                    xml.append(escapeXml(value.toString()));
                }
                xml.append("</").append(tagName).append(">\n");
            }
        }

        xml.append("</").append(rootName).append(">");
        return xml.toString();
    }

    private String getRootName(Class<?> clazz) {
        XmlRootAlias rootAnnotation = clazz.getAnnotation(XmlRootAlias.class);
        if (rootAnnotation != null && !rootAnnotation.value().isBlank()) {
            return rootAnnotation.value();
        }
        return clazz.getSimpleName().toUpperCase();
    }

    private Map<String, Object> extractMappedFields(Object model, Class<?> clazz) {
        Map<String, Object> fieldsMap = new LinkedHashMap<>();

        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    XmlFieldAlias alias = field.getAnnotation(XmlFieldAlias.class);
                    String tagName = (alias != null && !alias.value().isBlank()) ? alias.value() : field.getName();
                    Object value = field.get(model);
                    fieldsMap.put(tagName, value);
                } catch (IllegalAccessException e) {
                    throw new BusinessException("Falha ao acessar atributo para mapeamento XML: " + field.getName(), e);
                }
            }
            current = current.getSuperclass();
        }

        return fieldsMap;
    }

    private String escapeXml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
