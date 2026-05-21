# Guía Técnica: Template Java Spring Boot SMG

## Índice
1. [Descripción general](#1-descripción-general)
2. [Arquitectura](#2-arquitectura)
3. [Estructura del proyecto](#3-estructura-del-proyecto)
4. [Configuración inicial de un nuevo servicio](#4-configuración-inicial-de-un-nuevo-servicio)
5. [Convenciones de paquetes y clases](#5-convenciones-de-paquetes-y-clases)
6. [Cómo agregar un nuevo endpoint](#6-cómo-agregar-un-nuevo-endpoint)
7. [Configuración de base de datos Sybase](#7-configuración-de-base-de-datos-sybase)
8. [Variables de entorno](#8-variables-de-entorno)
9. [Manejo de errores](#9-manejo-de-errores)
10. [Infraestructura transversal](#10-infraestructura-transversal)
11. [Tests](#11-tests)
12. [Build y ejecución local](#12-build-y-ejecución-local)

---

## 1. Descripción general

Este proyecto es el **template base** para todos los microservicios Java de SMG. Provee configuración, arquitectura y estructura lista para usar. Al crear un nuevo servicio, se parte de este template y se agrega únicamente la lógica de negocio específica.

**Stack tecnológico:**

| Componente        | Tecnología              | Versión                  |
|-------------------|-------------------------|--------------------------|
| Lenguaje          | Java                    | 17                       |
| Framework         | Spring Boot             | 3.2.2                    |
| ORM               | MyBatis                 | 3.0.4                    |
| Base de datos prod| Sybase                  | jconn4 16.0.27363        |
| Base de datos test| H2                      | in-memory                |
| Connection pool   | HikariCP                | (incluido en Spring Boot)|
| Documentación API | springdoc-openapi       | 2.3.0                    |
| Build             | Maven                   | (mvnw incluido)          |

---

## 2. Arquitectura

El template implementa **Arquitectura Hexagonal (Ports & Adapters)**. La regla central es que la lógica de negocio no tiene dependencias hacia infraestructura (HTTP, base de datos, etc.).

```
┌─────────────────────────────────────────────────────────────┐
│                        ADAPTADORES                          │
│  ┌─────────────┐                       ┌─────────────────┐  │
│  │  Controller  │                       │ MyBatis Adapter │  │
│  │  (REST IN)   │                       │   (DB OUT)      │  │
│  └──────┬──────┘                       └────────┬────────┘  │
├─────────┼───────────────────────────────────────┼───────────┤
│         ▼           APLICACIÓN                  ▲           │
│  ┌─────────────┐                       ┌────────┴────────┐  │
│  │  Port IN    │                       │   Port OUT      │  │
│  │ (interfaz)  │                       │  (interfaz)     │  │
│  └──────┬──────┘                       └────────┬────────┘  │
│         ▼                                       │           │
│  ┌──────────────────────────────────────────────┴────────┐  │
│  │                      USE CASE                         │  │
│  │               (lógica de negocio pura)                │  │
│  └───────────────────────────────────────────────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                         DOMINIO                             │
│          (modelos, excepciones de dominio, helpers)         │
└─────────────────────────────────────────────────────────────┘
```

**Regla de dependencias**: las flechas siempre apuntan hacia adentro.
`Controller` → `PortIn` ← `UseCase` → `PortOut` ← `MyBatisAdapter`

El `UseCase` **nunca** importa clases de `adapter/`. El `Controller` **nunca** importa clases de `usecase/` directamente, solo la interfaz `PortIn`.

---

## 3. Estructura del proyecto

```
src/main/java/ar/com/smg/{servicio}/
├── {Servicio}Application.java                  ← @SpringBootApplication
│
├── adapter/
│   ├── controller/
│   │   └── {Servicio}ControllerAdapter.java    ← endpoints REST (@RestController)
│   ├── mybatis/
│   │   ├── {Entidad}MybatisAdapter.java        ← implementa PortOut
│   │   ├── mappers/
│   │   │   └── {Entidad}Mapper.java            ← interfaz @Mapper
│   │   └── model/
│   │       └── {Entidad}Model.java             ← DTO de parámetros para MyBatis
│   └── rest/
│       ├── exception/                          ← excepciones de clientes HTTP salientes
│       │   ├── BadRequestRestClientException.java
│       │   ├── ConflictRestClientException.java
│       │   ├── EmptyOrNullBodyRestClientException.java
│       │   ├── NonTargetRestClientException.java
│       │   ├── NotFoundRestClientException.java
│       │   ├── RestClientGenericException.java
│       │   └── TimeoutRestClientException.java
│       └── handler/
│           └── RestTemplateErrorHandler.java   ← mapea HTTP status → excepción
│
├── application/
│   ├── port/
│   │   ├── in/
│   │   │   └── {Accion}PortIn.java             ← interfaz que llama el Controller
│   │   └── out/
│   │       └── {Entidad}RepositoryPortOut.java ← interfaz que llama el UseCase
│   ├── usecase/
│   │   └── {Accion}UseCase.java               ← implementa PortIn, contiene la lógica
│   └── validator/
│       └── {Servicio}Validator.java            ← validaciones de parámetros de entrada
│
├── config/
│   ├── ErrorCode.java          ← enum con todos los códigos de error del servicio
│   ├── ErrorHandler.java       ← @ControllerAdvice, captura excepciones globalmente
│   ├── GenericException.java   ← clase abstracta base para excepciones personalizadas
│   ├── GlobalResponseWrapper.java ← envuelve todas las respuestas en ResponseWrapper
│   ├── MyBatisConfig.java      ← SqlSessionFactory + HikariCP + TransactionManager
│   ├── SwaggerConfig.java      ← título, descripción y contacto de la API
│   ├── TraceIdInterceptor.java ← inyecta traceId W3C en MDC y response header
│   └── exception/
│       ├── GlobalExceptionHandler.java
│       └── ResourceNotFoundException.java
│
└── domain/
    ├── ResponseWrapper.java            ← envelope genérico de respuesta <T>
    ├── {entidad}/
    │   └── {Entidad}.java              ← modelo puro (@Data @Builder, sin Spring)
    ├── exception/
    │   └── ValidationException.java   ← extiende GenericException con VALIDATION_ERROR
    └── helper/
        └── SybaseHelper.java          ← detecta RAISERROR de Sybase (error de negocio)

src/main/resources/
├── mybatis/mappers/
│   └── {Entidad}Mapper.xml    ← queries y stored procedures MyBatis
├── schema.sql                  ← DDL H2 para tests (se ejecuta automáticamente)
├── data.sql                    ← seed data H2 para tests
└── checkstyle.xml              ← reglas de estilo de código (falla el build si no cumple)

src/test/java/ar/com/smg/{servicio}/
├── adapter/controller/
│   └── {Servicio}ControllerAdapterTest.java   ← @WebMvcTest (MockMvc)
└── application/usecase/
    └── {Accion}UseCaseTest.java               ← @ExtendWith(MockitoExtension)
```

---

## 4. Configuración inicial de un nuevo servicio

### 4.1 Clonar el template

```bash
git clone <url-del-template> core-{nombre-servicio}
cd core-{nombre-servicio}
```

### 4.2 Checklist de renombrado

- [ ] **`pom.xml`**: cambiar `<artifactId>` y `<name>` a `core-{nombre-servicio}`
- [ ] **`application.yml`**: cambiar `spring.application.name` y `metrics.tags.application`
- [ ] **Paquete Java**: crear carpeta `ar/com/smg/{servicio}/`, mover todos los archivos actualizando la declaración `package` en cada uno
- [ ] **`MyBatisConfig.java`**: actualizar `setTypeAliasesPackage("ar.com.smg.{servicio}.domain")`
- [ ] **`ErrorCode.java`**: actualizar prefijo `CORE-TEMPLATE-*` → `CORE-{SERVICIO}-*`
- [ ] **`SwaggerConfig.java`**: actualizar título y descripción
- [ ] **Clase principal**: renombrar `TemplateApplication` → `{Servicio}Application`
- [ ] **Eliminar endpoint de ejemplo**: borrar `PingResponse`, `PingPortIn`, `PingUseCase`, `TemplateControllerAdapter` y sus tests

### 4.3 Instalar dependencia Sybase en una máquina nueva

El JAR `jconn4d-16.0.jar` está en la raíz del proyecto. El `pom.xml` tiene configurado el repositorio `libs/` que ya lo incluye. Si por alguna razón Maven no lo resuelve, ejecutar en PowerShell desde la raíz del proyecto:

```powershell
$dir = "$env:USERPROFILE\.m2\repository\com\sybase\jconn4\16.0.27363"
New-Item -ItemType Directory -Force -Path $dir | Out-Null
Copy-Item "jconn4d-16.0.jar" "$dir\jconn4-16.0.27363.jar"
# Limpiar caché de fallo si existe
Remove-Item "$dir\*.lastUpdated" -ErrorAction SilentlyContinue
```

---

## 5. Convenciones de paquetes y clases

| Capa             | Paquete                          | Nomenclatura                       |
|------------------|----------------------------------|------------------------------------|
| Controller       | `adapter/controller/`            | `{Entidad}ControllerAdapter`       |
| MyBatis adapter  | `adapter/mybatis/`               | `{Entidad}MybatisAdapter`          |
| Mapper interface | `adapter/mybatis/mappers/`       | `{Entidad}Mapper`                  |
| MyBatis DTO      | `adapter/mybatis/model/`         | `{Entidad}Model`                   |
| Input port       | `application/port/in/`           | `{Accion}PortIn`                   |
| Output port      | `application/port/out/`          | `{Entidad}RepositoryPortOut`       |
| Use case         | `application/usecase/`           | `{Accion}UseCase`                  |
| Validator        | `application/validator/`         | `{Servicio}Validator`              |
| Modelo dominio   | `domain/{entidad}/`              | `{Entidad}`                        |
| Mapper XML       | `resources/mybatis/mappers/`     | `{Entidad}Mapper.xml`              |

---

## 6. Cómo agregar un nuevo endpoint

### Ejemplo completo: `GET /api/v1/clientes/{id}`

#### Paso 1 — Modelo de dominio
```java
// domain/cliente/Cliente.java
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class Cliente {
    private Integer id;
    private String nombre;
    private String email;
}
```

#### Paso 2 — Input port
```java
// application/port/in/GetClientePortIn.java
public interface GetClientePortIn {
    Cliente getCliente(Integer id);
}
```

#### Paso 3 — Output port (si necesita base de datos)
```java
// application/port/out/ClienteRepositoryPortOut.java
public interface ClienteRepositoryPortOut {
    Cliente findById(Integer id);
}
```

#### Paso 4 — Use case
```java
// application/usecase/GetClienteUseCase.java
@Slf4j
@Component
@RequiredArgsConstructor
public class GetClienteUseCase implements GetClientePortIn {

    private final ClienteRepositoryPortOut clienteRepositoryPortOut;

    @Override
    public Cliente getCliente(Integer id) {
        if (id == null || id <= 0) {
            throw new ValidationException("El id debe ser un número positivo");
        }
        log.info("Buscando cliente con id: {}", id);
        return clienteRepositoryPortOut.findById(id);
    }
}
```

#### Paso 5 — MyBatis model (DTO de parámetros)
```java
// adapter/mybatis/model/ClienteModel.java
@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ClienteModel {
    private Integer id;
}
```

#### Paso 6 — Mapper interface
```java
// adapter/mybatis/mappers/ClienteMapper.java
@Mapper
public interface ClienteMapper {
    Cliente findById(ClienteModel model);
}
```

#### Paso 7 — Mapper XML
```xml
<!-- resources/mybatis/mappers/ClienteMapper.xml -->
<mapper namespace="ar.com.smg.{servicio}.adapter.mybatis.mappers.ClienteMapper">

    <resultMap id="clienteResult" type="Cliente">
        <result property="id"     column="cli_id"/>
        <result property="nombre" column="cli_nombre"/>
        <result property="email"  column="cli_email"/>
    </resultMap>

    <select id="findById" statementType="CALLABLE" resultMap="clienteResult">
        {call midb..sp_get_cliente(
            #{id, jdbcType=INTEGER, mode=IN}
        )}
    </select>

</mapper>
```

> El alias `Cliente` en `type="Cliente"` funciona porque `MyBatisConfig` registra el paquete `domain` como alias automáticamente.

#### Paso 8 — MyBatis adapter (implementa PortOut)
```java
// adapter/mybatis/ClienteMybatisAdapter.java
@Component
@RequiredArgsConstructor
public class ClienteMybatisAdapter implements ClienteRepositoryPortOut {

    private final ClienteMapper clienteMapper;

    @Override
    public Cliente findById(Integer id) {
        return clienteMapper.findById(ClienteModel.builder().id(id).build());
    }
}
```

#### Paso 9 — Controller
```java
// adapter/controller/{Servicio}ControllerAdapter.java
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class {Servicio}ControllerAdapter {

    private final GetClientePortIn getClientePortIn;

    @GetMapping("/clientes/{id}")
    @Operation(summary = "Obtiene un cliente por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno")
    })
    public ResponseEntity<Cliente> getCliente(@PathVariable Integer id) {
        return ResponseEntity.ok(getClientePortIn.getCliente(id));
    }
}
```

La respuesta HTTP real será automáticamente:
```json
{
  "resource": "/api/v1/clientes/1",
  "status": 200,
  "data": { "id": 1, "nombre": "Juan", "email": "juan@mail.com" },
  "timestamp": "2026-05-21T11:00:00",
  "metadata": { "traceId": "00-abc123-0000000000000000-01" }
}
```

---

## 7. Configuración de base de datos Sybase

### MyBatisConfig — puntos clave

```java
bean.setTypeAliasesPackage("ar.com.smg.{servicio}.domain");
// ↑ permite usar nombres simples en los XML (ej: "Cliente" en vez del FQCN)

bean.setMapperLocations("classpath:mybatis/mappers/*.xml");
// ↑ carga todos los XML automáticamente, agregar nuevos archivos no requiere config
```

### Tipos JDBC más usados en stored procedures

| Java          | jdbcType    |
|---------------|-------------|
| `Integer`     | `INTEGER`   |
| `String`      | `VARCHAR`   |
| `BigDecimal`  | `NUMERIC`   |
| `Date`        | `TIMESTAMP` |
| `Boolean`     | `BIT`       |

### HikariCP — configuración de producción en `application.yml`

```yaml
datasource:
  hikari:
    minimum-idle: 15
    maximum-pool-size: 50
    connection-timeout: 30000       # 30 seg — tiempo máximo para obtener conexión del pool
    max-lifetime: 600000            # 10 min — vida máxima de una conexión
    idle-timeout: 120000            # 2 min  — tiempo máximo idle antes de cerrar
    connection-test-query: select 1 # query para validar que la conexión sigue viva
```

### SybaseHelper — RAISERROR desde stored procedures

Cuando un stored procedure lanza `RAISERROR` con código `99999`, el `ErrorHandler` lo detecta y devuelve HTTP 400 con el mensaje del error (en vez de HTTP 500). Para agregar más códigos:

```java
// domain/helper/SybaseHelper.java
private static final Integer[] RAISERRORS = {99999, 12345}; // agregar acá
```

---

## 8. Variables de entorno

| Variable          | Descripción                   | Ejemplo                                  |
|-------------------|-------------------------------|------------------------------------------|
| `DB_URL`          | JDBC URL de Sybase            | `jdbc:sybase:Tds:host:5000/dbname`       |
| `DB_DRIVER_CLASS` | Driver JDBC                   | `com.sybase.jdbc4.jdbc.SybDriver`        |
| `DB_USER`         | Usuario de base de datos      | `mi_usuario`                             |
| `DB_PASSWORD`     | Contraseña de base de datos   | `mi_password`                            |

### Perfiles de Spring

| Perfil | Uso              | Actuator expuesto                |
|--------|------------------|----------------------------------|
| `local`| Desarrollo local | Todo excepto `env` y `beans`     |
| `dev`  | Ambiente dev     | Todo excepto `env` y `beans`     |
| `qa`   | QA               | Solo `health`                    |
| `pre`  | Pre-producción   | Solo `health`                    |
| `prod` | Producción       | Solo `health`                    |

Activar perfil al correr:
```bash
java -jar app.jar --spring.profiles.active=local
```

---

## 9. Manejo de errores

### Excepciones disponibles

| Excepción                   | Cuándo usarla                               | HTTP |
|-----------------------------|---------------------------------------------|------|
| `ValidationException`       | Validaciones de negocio en use case/validator | 400  |
| `ResourceNotFoundException` | Recurso no encontrado                        | 404  |
| Extender `GenericException` | Errores personalizados con `ErrorCode` propio | variable |

### Agregar un código de error en `ErrorCode.java`

```java
MI_ERROR_ESPECIFICO(120, "120", "Descripción legible", "CORE-{SERVICIO}-MI_ERROR"),
```

Campos del enum: `(valor numérico, status string, descripción, código de sistema)`.

### Respuesta de error estándar (automática)

```json
{
  "name": "Bad Request",
  "status": 400,
  "code": 115,
  "error_internal_code": 115,
  "error_description": "Error de validación",
  "error_code": "CORE-{SERVICIO}-VALIDATION_ERROR",
  "timestamp": "2026-05-21T11:00:00",
  "resource": "/api/v1/clientes/abc",
  "detail": "El id debe ser un número positivo",
  "metadata": { "query_string": "" }
}
```

---

## 10. Infraestructura transversal

Todo lo siguiente es **automático** — no requiere ninguna configuración en el código de negocio.

### TraceIdInterceptor
- Lee el header `traceparent` del request entrante (formato W3C Trace Context)
- Si no viene, genera uno: `00-{UUID sin guiones}-0000000000000000-01`
- Lo guarda en MDC → aparece en todos los logs: `INFO [trace=00-abc123-...]`
- Lo devuelve en el header `traceparent` del response
- Está disponible en `ResponseWrapper.metadata.traceId`

Para propagarlo al llamar a otro servicio:
```java
headers.set("traceparent", MDC.get("traceId"));
```

### GlobalResponseWrapper
Intercepta todas las respuestas y las envuelve en:
```json
{
  "resource": "/api/v1/...",
  "status": 200,
  "data": { ... },
  "timestamp": "...",
  "metadata": { "traceId": "..." }
}
```
El `Controller` devuelve `ResponseEntity<MiModelo>` y el wrapper aplica solo.

### ErrorHandler
`@ControllerAdvice` que captura excepciones y construye la respuesta de error estándar. La flag `hideException` oculta el detalle técnico en errores 500 (devuelve "Error interno" genérico).

---

## 11. Tests

### Test de controller — `@WebMvcTest`

```java
@WebMvcTest({Servicio}ControllerAdapter.class)
class {Servicio}ControllerAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetClientePortIn getClientePortIn;  // mockear el PORT, no el use case

    @Test
    void getCliente_ShouldReturnOk_WhenValidId() throws Exception {
        Cliente mockCliente = Cliente.builder().id(1).nombre("Juan").build();
        when(getClientePortIn.getCliente(1)).thenReturn(mockCliente);

        mockMvc.perform(get("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nombre").value("Juan"));
    }

    @Test
    void getCliente_ShouldReturn400_WhenValidationException() throws Exception {
        when(getClientePortIn.getCliente(-1))
                .thenThrow(new ValidationException("El id debe ser positivo"));

        mockMvc.perform(get("/api/v1/clientes/-1"))
                .andExpect(status().isBadRequest());
    }
}
```

### Test de use case — `@ExtendWith(MockitoExtension)`

```java
@ExtendWith(MockitoExtension.class)
class GetClienteUseCaseTest {

    @Mock
    private ClienteRepositoryPortOut repo;  // mockear el PORT OUT

    @InjectMocks
    private GetClienteUseCase useCase;

    @Test
    void getCliente_ShouldReturnCliente_WhenValidId() {
        Cliente mockCliente = Cliente.builder().id(1).nombre("Juan").build();
        when(repo.findById(1)).thenReturn(mockCliente);

        Cliente resultado = useCase.getCliente(1);

        assertThat(resultado.getNombre()).isEqualTo("Juan");
        verify(repo, times(1)).findById(1);
    }

    @Test
    void getCliente_ShouldThrowValidation_WhenIdIsNull() {
        assertThrows(ValidationException.class, () -> useCase.getCliente(null));
        verifyNoInteractions(repo);
    }
}
```

### H2 para tests
Los tests corren contra H2 (in-memory), **nunca contra Sybase**. Usar `schema.sql` y `data.sql` para preparar datos:

```sql
-- schema.sql
CREATE TABLE IF NOT EXISTS clientes (
    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    nombre  VARCHAR(100) NOT NULL,
    email   VARCHAR(200)
);

-- data.sql
INSERT INTO clientes (nombre, email) VALUES ('Juan', 'juan@mail.com');
```

---

## 12. Build y ejecución local

### Comandos esenciales

```bash
# Compilar y ejecutar todos los tests
./mvnw clean test

# Ejecutar localmente (requiere variables de entorno o application-local.yml)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Generar JAR ejecutable
./mvnw clean package -DskipTests
java -jar target/core-{servicio}-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

### URLs disponibles en local

| URL                                       | Descripción                       |
|-------------------------------------------|-----------------------------------|
| `http://localhost:8080/swagger-ui.html`   | Documentación interactiva de la API |
| `http://localhost:8080/api/docs`          | OpenAPI spec en formato JSON      |
| `http://localhost:8080/actuator/health`   | Estado del servicio               |

### Nota para Windows

El plugin de git hooks solo se ejecuta en Linux/Mac (está en el profile `git-hooks` con activación `<os><family>unix</family></os>`). En Windows el build funciona sin ninguna configuración adicional.
