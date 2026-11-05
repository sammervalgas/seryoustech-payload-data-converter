# Payload Data Converter - REST API

API REST desenvolvida com **Spring Boot 3**, **Java 21**, **Spring Data JPA**, banco de dados em memória **H2** e documentação interativa via **Swagger UI (SpringDoc OpenAPI)**, sem autenticação e estruturada com estrito isolamento de responsabilidades.

---

## 🚀 Tecnologias Utilizadas

- **Java 21 (LTS)**: Records, Pattern Matching, Switch Expressions
- **Spring Boot 3.4.1**: Framework base para APIs corporativas
- **Project Lombok**: Eliminação de boilerplate (@Getter, @Setter, @Data, @Builder, @RequiredArgsConstructor, @Slf4j)
- **Spring AOP**: Aspect-Oriented Programming para conversão transparente de modelos
- **Jackson XML Dataformat**: Serialização e processamento flexível de XML
- **Spring Data JPA & Hibernate**: Camada de persistência relacional
- **H2 Database**: Banco de dados em memória de alta performance
- **SpringDoc OpenAPI 2.8.0**: Documentação viva (Swagger 3)
- **Bean Validation (Hibernate Validator)**: Validação declarativa de entrada de dados
- **Maven**: Gerenciamento de ciclo de vida e dependências
- **JUnit 5 & Mockito**: Testes automatizados unitários e de contexto

---

## 📂 Arquitetura e Estrutura de Pacotes

O projeto segue os princípios de separação de responsabilidades (Layered Architecture):

## 📂 Arquitetura e Separação de Estruturas REST

O projeto possui **duas estruturas REST completamente isoladas por pacotes e responsabilidades**, além de um pacote compartilhado de infraestrutura:

```text
src/main/java/br/com/seryoustech/
├── PayloadDataConverterApplication.java       # Ponto de entrada (Main)
│
├── standard/                                 # 🔵 ESTRUTURA REST PADRÃO (Camadas / CRUD)
│   ├── controller/
│   │   ├── ProductMAPI.java                  # Contrato REST (Swagger + rotas HTTP)
│   │   └── ProductController.java            # Implementação limpa (@RestController)
│   ├── dto/
│   │   ├── ProductRequestDTO.java            # Record de entrada com validações
│   │   └── ProductResponseDTO.java           # Record de saída
│   ├── mapper/
│   │   └── ProductMapper.java                # Conversor DTO <-> Entidade
│   ├── model/
│   │   └── Product.java                      # Entidade JPA (tb_products)
│   ├── repository/
│   │   └── ProductRepository.java            # Interface Spring Data JPA
│   └── service/
│       ├── ProductService.java               # Interface de regras de negócio
│       └── impl/
│           └── ProductServiceImpl.java       # Implementação transacional
│
├── converter/                                # 🟢 ESTRUTURA REST CONVERSOR (Hexagonal / Ports & Adapters)
│   ├── adapter/
│   │   ├── in/web/                           # Driving Adapter (Entrada Web)
│   │   │   ├── ConverterMAPI.java            # Contrato REST do conversor
│   │   │   └── ConverterController.java      # Implementação REST do conversor
│   │   └── out/xml/                          # Driven Adapter (Saída XML)
│   │       └── JacksonXmlTransformationAdapter.java # Serialização XML com aliases
│   ├── port/
│   │   ├── in/
│   │   │   └── ConvertPayloadUseCase.java    # Driving Port (Caso de Uso de Conversão)
│   │   └── out/
│   │       └── XmlTransformationPort.java    # Driven Port (SPI de Transformação)
│   ├── model/
│   │   └── ProductPayload.java               # Modelo de dados com @XmlFieldAlias
│   ├── annotation/                           # Anotações customizadas do conversor
│   │   ├── AutoConvertToXml.java             # Ativação do Aspecto AOP
│   │   ├── XmlFieldAlias.java                # Mapeamento do nome da tag XML
│   │   └── XmlRootAlias.java                 # Mapeamento da tag raiz XML
│   ├── aspect/
│   │   └── XmlConversionAspect.java          # Interceptador AOP para conversão automática
│   └── service/
│       └── impl/
│           └── PayloadConverterServiceImpl.java # Orquestrador de conversão JSON -> XML
│
└── shared/                                   # ⚪ INFRAESTRUTURA COMPARTILHADA
    ├── config/
    │   └── OpenApiConfig.java                # Configuração OpenAPI / Swagger UI agrupada
    └── exception/
        ├── BusinessException.java            # Erro de negócio (HTTP 422)
        ├── ErrorResponseDTO.java             # Payload padronizado de erro
        ├── GlobalExceptionHandler.java       # Interceptador global (@RestControllerAdvice)
        └── ResourceNotFoundException.java    # Erro de recurso não encontrado (HTTP 404)
```

---

## 🛠️ Como Executar a Aplicação

### Pré-requisitos
- **Java 21** instalado e configurado no `PATH` (`java -version`).
- **Apache Maven 3.9+** instalado (`mvn -version`).

### Passos para Execução

1. **Compilar e baixar dependências**:
   ```bash
   mvn clean package
   ```

2. **Executar os testes automatizados**:
   ```bash
   mvn test
   ```

3. **Iniciar o servidor Spring Boot**:
   ```bash
   mvn spring-boot:run
   ```

A API iniciará por padrão na porta `8080`.

---

## 📖 Documentação Interativa & Console do Banco

| Recurso | URL | Observações |
| :--- | :--- | :--- |
| **Swagger UI** | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) | Interface gráfica para testar todos os endpoints |
| **OpenAPI Spec (JSON)** | [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) | Especificação OpenAPI 3 em formato JSON |
| **H2 Web Console** | [http://localhost:8080/h2-console](http://localhost:8080/h2-console) | Visualizador do banco em memória |

### Credenciais do Console H2
- **JDBC URL**: `jdbc:h2:mem:converterdb`
- **Driver Class**: `org.h2.Driver`
- **User Name**: `sa`
- **Password**: *(deixar em branco)*

---

## 🔄 Port & Adapter: Conversão JSON para XML com Aliases

A arquitetura hexagonal isola a transformação de formatos por meio de **Portas e Adaptadores**:

### 1. Modelo de Domínio Anotado
Adicione as anotações no modelo de dados para determinar a correspondência entre a chave JSON de entrada e a tag XML de saída:

```java
@XmlRootAlias("PRODUTO")
public class ProductPayload {

    @XmlFieldAlias("CODPROD")
    private String codigoProduto;

    @XmlFieldAlias("NOMEPROD")
    private String nomeProduto;

    @XmlFieldAlias("VLRPRECO")
    private BigDecimal valorPreco;

    @XmlFieldAlias("QTDESTOQUE")
    private Integer quantidadeEstoque;

    @XmlFieldAlias("STATUSATIVO")
    private Boolean statusAtivo;
    // ...
}
```

### 2. Conversão Transparente via Aspecto AOP (`@AutoConvertToXml`)
No Controller, anote o método com `@AutoConvertToXml` e retorne o objeto normalmente. O Aspecto intercepta o resultado e serializa automaticamente para XML com os aliases definidos:

```java
@PostMapping(value = "/aspect-conversion", produces = MediaType.APPLICATION_XML_VALUE)
@AutoConvertToXml
public ResponseEntity<Object> convertViaAspect(@RequestBody ProductPayload payload) {
    return ResponseEntity.ok(payload); // O Aspect intercepta e converte para XML!
}
```

---

## 📡 Endpoints da API

### Módulo Conversor (`/api/v1/converter`)
| Método | Rota | Descrição | Content-Type | Retorno |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/converter/json-to-xml` | Converte payload JSON string para XML | `application/json` | `application/xml` |
| `POST` | `/api/v1/converter/upload` | Upload de arquivo `.json` gerando XML | `multipart/form-data` | `application/xml` |
| `POST` | `/api/v1/converter/aspect-conversion` | Conversão automática transparente via AOP | `application/json` | `application/xml` |

### Módulo CRUD de Produtos (`/api/v1/products`)

| Método | Rota | Descrição | Status Sucesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/products` | Cadastra um novo produto | `201 Created` |
| `GET` | `/api/v1/products` | Lista todos os produtos (`?activeOnly=true` para ativos) | `200 OK` |
| `GET` | `/api/v1/products/{id}` | Busca produto por ID | `200 OK` |
| `PUT` | `/api/v1/products/{id}` | Atualiza integralmente os dados do produto | `200 OK` |
| `PATCH` | `/api/v1/products/{id}/status` | Alterna status ativo/inativo (`?active=true/false`) | `200 OK` |
| `DELETE` | `/api/v1/products/{id}` | Exclui permanentemente um produto | `204 No Content` |

---

## 🧪 Exemplos de Requisições com cURL

### 1. Cadastrar Produto (POST)
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Cadeira Ergonômica Pro",
    "description": "Cadeira com ajuste lombar e encosto em mesh",
    "price": 899.90,
    "stockQuantity": 15,
    "active": true
  }'
```

### 2. Listar Produtos Ativos (GET)
```bash
curl -X GET "http://localhost:8080/api/v1/products?activeOnly=true"
```

### 3. Buscar Produto por ID (GET)
```bash
curl -X GET http://localhost:8080/api/v1/products/1
```

### 4. Atualizar Produto (PUT)
```bash
curl -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teclado Mecânico RGB Pro",
    "description": "Teclado mecânico switch brown padrão ABNT2 com apoio de pulso",
    "price": 389.90,
    "stockQuantity": 20,
    "active": true
  }'
```

### 5. Inativar Produto (PATCH)
```bash
curl -X PATCH "http://localhost:8080/api/v1/products/1/status?active=false"
```

### 7. Conversão de JSON para XML via Endpoint REST
```bash
curl -X POST http://localhost:8080/api/v1/converter/json-to-xml \
  -H "Content-Type: application/json" \
  -d '{
    "codigoProduto": "PRD-99",
    "nomeProduto": "Monitor Gamer 144Hz",
    "valorPreco": 1899.90,
    "quantidadeEstoque": 8,
    "statusAtivo": true
  }'
```
**Saída XML retornada:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<PRODUTO>
  <CODPROD>PRD-99</CODPROD>
  <NOMEPROD>Monitor Gamer 144Hz</NOMEPROD>
  <VLRPRECO>1899.90</VLRPRECO>
  <QTDESTOQUE>8</QTDESTOQUE>
  <STATUSATIVO>true</STATUSATIVO>
</PRODUTO>
```

### 8. Conversão via Aspecto AOP (@AutoConvertToXml)
```bash
curl -X POST http://localhost:8080/api/v1/converter/aspect-conversion \
  -H "Content-Type: application/json" \
  -d '{
    "codigoProduto": "PRD-AOP-01",
    "nomeProduto": "Mouse Sem Fio",
    "valorPreco": 99.00,
    "quantidadeEstoque": 100,
    "statusAtivo": true
  }'
```

---

## 🛡️ Tratamento de Erros

Respostas de erro seguem formato uniforme com status code apropriado:
```json
{
  "timestamp": "2026-11-05T10:45:00",
  "status": 400,
  "error": "Erro de Validação",
  "message": "Um ou mais campos contêm valores inválidos",
  "path": "/api/v1/products",
  "fieldErrors": {
    "price": "O preço deve ser estritamente maior que zero",
    "name": "O nome do produto é obrigatório"
  }
}
```
