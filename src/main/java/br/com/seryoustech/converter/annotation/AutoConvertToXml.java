package br.com.seryoustech.converter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação de método que aciona o Aspecto AOP para conversão transparente do modelo para XML.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoConvertToXml {

    String root() default "";
}
