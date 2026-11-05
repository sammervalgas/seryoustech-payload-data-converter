package br.com.seryoustech.converter.aspect;

import br.com.seryoustech.converter.annotation.AutoConvertToXml;
import br.com.seryoustech.converter.port.out.XmlTransformationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Aspecto AOP pertencente à estrutura do conversor.
 * Intercepta métodos com @AutoConvertToXml e converte para XML.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class XmlConversionAspect {

    private final XmlTransformationPort xmlTransformationPort;

    @Around("@annotation(autoConvertToXml)")
    public Object interceptAndConvert(ProceedingJoinPoint joinPoint, AutoConvertToXml autoConvertToXml) throws Throwable {
        log.debug("[REST Conversor - Aspect] Interceptando método '{}'", joinPoint.getSignature().toShortString());

        Object result = joinPoint.proceed();

        if (result == null) {
            return null;
        }

        if (result instanceof ResponseEntity<?> responseEntity) {
            Object body = responseEntity.getBody();
            if (body == null) {
                return result;
            }

            String xml = convertModelToXml(body, autoConvertToXml);

            return ResponseEntity.status(responseEntity.getStatusCode())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                    .body(xml);
        }

        return convertModelToXml(result, autoConvertToXml);
    }

    private String convertModelToXml(Object model, AutoConvertToXml autoConvertToXml) {
        if (!autoConvertToXml.root().isBlank()) {
            String customRoot = autoConvertToXml.root();
            String defaultXml = xmlTransformationPort.transformToXml(model);
            return replaceRootTag(defaultXml, customRoot);
        }

        return xmlTransformationPort.transformToXml(model);
    }

    private String replaceRootTag(String xml, String newRoot) {
        return xml.replaceAll("<ROOT>", "<" + newRoot + ">")
                  .replaceAll("</ROOT>", "</" + newRoot + ">");
    }
}
