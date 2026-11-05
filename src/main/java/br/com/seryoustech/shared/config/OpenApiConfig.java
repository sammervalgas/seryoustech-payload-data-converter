package br.com.seryoustech.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração centralizada da documentação OpenAPI / Swagger UI com distinção de módulos.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payload Data Converter & REST API")
                        .description("Projeto Spring Boot 3 com separação explícita entre a estrutura REST Padrão (CRUD em Camadas) "
                                + "e a estrutura REST do Conversor (Arquitetura Hexagonal Ports & Adapters com AOP).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Seryous Tech")
                                .email("contato@seryoustech.com.br")
                                .url("https://seryoustech.com.br"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .tags(List.of(
                        new Tag().name("1. REST Padrão - Produtos").description("Módulo CRUD tradicional estruturado em camadas"),
                        new Tag().name("2. REST Conversor - Ports & Adapters").description("Módulo de conversão JSON para XML utilizando Arquitetura Hexagonal e Aspectos AOP")
                ))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Ambiente Local de Desenvolvimento")
                ));
    }
}
