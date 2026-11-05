# 📋 Template de Prompt: API REST Spring Boot 3 Completa (Java 21 + Hexagonal + Camadas + AOP + H2 + Swagger)

Copie e personalize o prompt abaixo para iniciar novos projetos com o mesmo padrão arquitetural e nível de excelência:

```markdown
Você é um Arquiteto de Software Java Sênior. Crie um projeto Spring Boot 3 completo, robusto e pronto para produção, seguindo rigorosamente as especificações abaixo:

---

### 1. Parâmetros Gerais do Projeto
- **Linguagem**: Java 21 (LTS) — utilizando Records, Pattern Matching, Switch Expressions
- **Framework**: Spring Boot 3.4.x (Maven)
- **Nome do Projeto / ArtifactId**: [EX: payload-data-converter]
- **Pacote Base / GroupId**: [EX: br.com.seryoustech]
- **Segurança**: Sem autenticação inicial (rotas públicas)
- **Banco de Dados**: H2 Database (em memória, com console web habilitado em `/h2-console`)
- **Documentação da API**: SpringDoc OpenAPI 3 / Swagger UI (`/swagger-ui.html`)
- **Redução de Boilerplate**: Project Lombok (`@Data`, `@Builder`, `@RequiredArgsConstructor`, `@Slf4j`)
- **AOP**: Spring AOP (`spring-boot-starter-aop`)
- **Processamento XML**: Jackson XML Dataformat (`jackson-dataformat-xml`)
- **Validação**: Jakarta Bean Validation (`spring-boot-starter-validation`)

---

### 2. Separação Estrita de Módulos e Responsabilidades
O projeto deve ter três pilares de pacotes estritamente isolados:

#### 🔵 Módulo 1: REST Padrão (Arquitetura em Camadas / CRUD)
Domínio de exemplo: [EX: Product / Produto]
- **`controller/`**:
  - Interface com sufixo `MAPI` (ex: `ProductMAPI`): deve conter todo o contrato REST (`@RequestMapping`, `@GetMapping`, `@PostMapping`, etc.), validações (`@Valid`, `@PathVariable`) e anotações completas do OpenAPI/Swagger (`@Tag`, `@Operation`, `@ApiResponses`).
  - Classe Controller (ex: `ProductController`): implementa a interface `MAPI`, contém apenas `@RestController`, injeção de dependências via `@RequiredArgsConstructor` (Lombok) e delegação direta para o service.
- **`dto/`**: Java Records imutáveis para Request e Response (`ProductRequestDTO`, `ProductResponseDTO`) com validações declarativas (`@NotBlank`, `@Positive`, etc.).
- **`mapper/`**: Componente desacoplado (`ProductMapper`) para conversão e sincronização entre DTOs e a Entidade.
- **`model/`**: Entidade JPA anotada (`@Entity`, `@Table(name = "tb_products")`) com `@Getter`, `@Setter`, `@Builder`, `@EqualsAndHashCode(onlyExplicitlyIncluded = true)`, `@PrePersist` e `@PreUpdate`.
- **`repository/`**: Interface Spring Data JPA estendendo `JpaRepository` com métodos derivados de consulta.
- **`service/` e `service/impl/`**: Interface definindo o contrato de negócio (`ProductService`) e classe de implementação (`ProductServiceImpl`) com anotações `@Service`, `@Transactional(readOnly = true)` e `@Slf4j`.

#### 🟢 Módulo 2: REST Conversor (Arquitetura Hexagonal / Ports & Adapters + Aspect AOP)
Conversão de payload de entrada (JSON / Arquivo) para saída XML com mapeamento de aliases:
- **`port/in/` (Driving Port)**: Interface de caso de uso (ex: `ConvertPayloadUseCase`) definindo conversão de JSON String, MultipartFile e Objeto.
- **`port/out/` (Driven Port)**: Interface SPI (ex: `XmlTransformationPort`) definindo o contrato para serialização XML.
- **`adapter/in/web/` (Driving Adapter)**: Interface `ConverterMAPI` + Controller `ConverterController` com rotas para conversão direta, upload de arquivo e acionamento via Aspecto.
- **`adapter/out/xml/` (Driven Adapter)**: Implementação `JacksonXmlTransformationAdapter` que lê anotações via Reflection e gera XML formatado e seguro contra injeção de caracteres especiais.
- **`annotation/`**: Anotações customizadas:
  - `@XmlFieldAlias("NOME_TAG")`: mapeia campo JSON para nome da tag XML.
  - `@XmlRootAlias("ROOT_TAG")`: define a tag raiz XML.
  - `@AutoConvertToXml`: anotação de método para acionamento automático de aspecto.
- **`aspect/`**: Classe `@Aspect` (`XmlConversionAspect`) com `@Around("@annotation(...)")` que intercepta o retorno do controller/service e transforma automaticamente o modelo retornado em XML com header `Content-Type: application/xml`.
- **`model/`**: Modelo de dados anotado (ex: `ProductPayload`) com `@Data`, `@Builder`, `@XmlRootAlias` e `@XmlFieldAlias`.
- **`service/impl/`**: Implementação do caso de uso (`PayloadConverterServiceImpl`) orquestrando parse de JSON e delegação à porta de saída.

#### ⚪ Módulo 3: Infraestrutura Compartilhada (`shared`)
- **`config/`**: `OpenApiConfig.java` com metadados do projeto, versão, licença, contato e tags customizadas para organizar o Swagger em grupos visuais.
- **`exception/`**:
  - `GlobalExceptionHandler.java`: Interceptador centralizado `@RestControllerAdvice` capturando validações (`MethodArgumentNotValidException`), recursos inexistentes (404), erros de negócio (422) e exceções genéricas (500).
  - Exceções customizadas: `ResourceNotFoundException`, `BusinessException`.
  - `ErrorResponseDTO.java`: Record padronizado para respostas de erro com timestamp, status, mensagem, path e fieldErrors.

---

### 3. Configurações e Recursos
- **`application.yml`**: Configurado com datasource do H2 em memória (`jdbc:h2:mem:converterdb`), console web habilitado em `/h2-console`, JPA com `ddl-auto: update`, SQL formatting ativo e rotas do Swagger (`/swagger-ui.html`).
- **`data.sql`**: Carga inicial de dados para testes imediatos após o boot.
- **`.gitignore`**: Arquivo completo e detalhado bloqueando `/target`, arquivos de build, logs, temporários de SO (macOS, Windows, Linux) e metadados de IDEs (IntelliJ, Eclipse, VS Code).
- **`README.md`**: Documentação técnica contendo visão geral, estrutura em árvore, instruções de execução (`mvn spring-boot:run`), credenciais do console H2 e exemplos completos de cURL para todos os endpoints.

---

### 4. Testes Automatizados
- Testes unitários com JUnit 5 e Mockito para os serviços do CRUD e do Conversor.
- Teste de carga de contexto Spring Boot (`@SpringBootTest`).

Gere todos os arquivos completos, sem comentários do tipo `// adicione os outros métodos aqui`, garantindo código limpo, moderno, legível e compilável.
```
