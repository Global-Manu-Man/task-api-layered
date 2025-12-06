# 📋 Task Management API

API REST para la gestión de tareas desarrollada con **Spring Boot 4.0** y **Java 17**.

[![Build Status](https://dev.azure.com/sandoval-org/task-api/_apis/build/status/task-api?branchName=main)](https://dev.azure.com/sandoval-org/task-api/_build)
[![Azure App Service](https://img.shields.io/badge/Azure-Deployed-blue)](https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net)

---

## 🌐 Demo en Vivo

| Recurso | URL |
|---------|-----|
| **API Base** | [https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net](https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net) |
| **Swagger UI** | [https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net/swagger-ui.html](https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net/swagger-ui.html) |
| **OpenAPI Spec** | [https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net/api-docs](https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net/api-docs) |
| **Azure DevOps** | [https://dev.azure.com/sandoval-org/task-api](https://dev.azure.com/sandoval-org/task-api) |

---

## 📖 Tabla de Contenidos

1. [Clonar el Proyecto](#-clonar-el-proyecto)
2. [Requisitos Previos](#-requisitos-previos)
3. [Construir el Proyecto](#-construir-el-proyecto)
4. [Ejecutar el Proyecto](#-ejecutar-el-proyecto)
5. [Endpoints de la API](#-endpoints-de-la-api)
6. [Arquitectura y Patrones de Diseño](#-arquitectura-y-patrones-de-diseño)
7. [Estructura del Proyecto](#-estructura-del-proyecto)
8. [CI/CD Pipeline](#-cicd-pipeline)
9. [Tecnologías Utilizadas](#-tecnologías-utilizadas)

---

## 📥 Clonar el Proyecto

### Opción 1: HTTPS
```bash
git clone https://dev.azure.com/sandoval-org/task-api/_git/task-api
```

### Opción 2: SSH
```bash
git clone git@ssh.dev.azure.com:v3/sandoval-org/task-api/task-api
```

### Entrar al directorio
```bash
cd task-api
```

---

## 📋 Requisitos Previos

Antes de construir y ejecutar el proyecto, asegúrate de tener instalado:

| Requisito | Versión Mínima | Verificar Instalación |
|-----------|----------------|----------------------|
| **Java JDK** | 17 o superior | `java -version` |
| **Maven** | 3.9+ | `mvn -version` |
| **Git** | 2.x | `git --version` |

### Instalar Java 17

**Windows (con Chocolatey):**
```powershell
choco install openjdk17
```

**macOS (con Homebrew):**
```bash
brew install openjdk@17
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

### Instalar Maven

**Windows (con Chocolatey):**
```powershell
choco install maven
```

**macOS (con Homebrew):**
```bash
brew install maven
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt install maven
```

---

## 🔨 Construir el Proyecto

### 1. Compilar el código
```bash
mvn clean compile
```

### 2. Ejecutar pruebas
```bash
mvn test
```

### 3. Empaquetar (crear JAR ejecutable)
```bash
mvn clean package
```

### 4. Compilar sin ejecutar tests (más rápido)
```bash
mvn clean package -DskipTests
```

El archivo JAR se genera en: `target/task-api-1.0.0.jar`

### 5. Instalar dependencias y construir
```bash
mvn clean install
```

---

## 🚀 Ejecutar el Proyecto

### Opción 1: Con Maven (Desarrollo)
```bash
mvn spring-boot:run
```

### Opción 2: Con el JAR (Producción)
```bash
java -jar target/task-api-1.0.0.jar
```

### Opción 3: Con perfil específico
```bash
# Perfil de desarrollo (H2)
java -jar target/task-api-1.0.0.jar --spring.profiles.active=dev

# Perfil de producción (PostgreSQL)
java -jar target/task-api-1.0.0.jar --spring.profiles.active=prod
```

### Verificar que está corriendo

Una vez iniciada la aplicación, verifica que funciona:

```bash
# Health check
curl http://localhost:8080/api/tasks

# O abre en el navegador
http://localhost:8080/swagger-ui.html
```

### URLs Locales Disponibles

| Recurso | URL |
|---------|-----|
| API Tasks | http://localhost:8080/api/tasks |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |
| H2 Console | http://localhost:8080/h2-console |

---

## 🛣️ Endpoints de la API

### Gestión de Tareas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/tasks` | Crear nueva tarea |
| `GET` | `/api/tasks` | Obtener todas las tareas |
| `GET` | `/api/tasks/{id}` | Obtener tarea por ID |
| `PUT` | `/api/tasks/{id}` | Actualizar tarea existente |
| `DELETE` | `/api/tasks/{id}` | Eliminar tarea |
| `GET` | `/api/tasks/status/{status}` | Filtrar por estado |
| `GET` | `/api/tasks/search?q={term}` | Buscar tareas |
| `GET` | `/api/tasks/stats` | Obtener estadísticas |

### Estados Disponibles

| Estado | Descripción |
|--------|-------------|
| `PENDING` | Tarea pendiente de iniciar |
| `IN_PROGRESS` | Tarea en progreso |
| `COMPLETED` | Tarea completada |

### Ejemplos de Uso

**Crear una tarea:**
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mi primera tarea",
    "description": "Descripción de la tarea",
    "status": "PENDING"
  }'
```

**Obtener todas las tareas:**
```bash
curl http://localhost:8080/api/tasks
```

**Actualizar una tarea:**
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Tarea actualizada",
    "status": "IN_PROGRESS"
  }'
```

**Eliminar una tarea:**
```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

---

## 🏗️ Arquitectura y Patrones de Diseño

### Arquitectura en Capas (Layered Architecture)

El proyecto implementa una **arquitectura en capas** que separa las responsabilidades de forma clara:

```
┌─────────────────────────────────────────────────────────────┐
│                   PRESENTATION LAYER                         │
│                  (Controllers + DTOs)                        │
│        Responsabilidad: Manejo de peticiones HTTP            │
├─────────────────────────────────────────────────────────────┤
│                    SERVICE LAYER                             │
│                     (Services)                               │
│        Responsabilidad: Lógica de negocio                    │
├─────────────────────────────────────────────────────────────┤
│                   PERSISTENCE LAYER                          │
│                   (Repositories)                             │
│        Responsabilidad: Acceso a datos                       │
├─────────────────────────────────────────────────────────────┤
│                     DOMAIN LAYER                             │
│                 (Entities + Enums)                           │
│        Responsabilidad: Modelo de dominio                    │
└─────────────────────────────────────────────────────────────┘
```

### Patrones de Diseño Aplicados

#### 1. Repository Pattern

**¿Qué es?** Patrón que abstrae el acceso a datos mediante interfaces.

**¿Por qué se usó?**
- Desacopla la lógica de negocio de la implementación de persistencia
- Facilita las pruebas unitarias mediante mocks
- Permite cambiar la base de datos sin modificar los servicios
- Centraliza las queries en un solo lugar

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> searchByTitleOrDescription(String term);
}
```

#### 2. DTO Pattern (Data Transfer Object)

**¿Qué es?** Objetos específicos para transferir datos entre capas.

**¿Por qué se usó?**
- Control sobre qué datos se exponen al cliente
- Validaciones específicas por operación (crear vs actualizar)
- Evita problemas de serialización circular con entidades JPA
- Permite evolucionar la API independientemente del modelo de datos

```java
// Request DTO - Solo campos necesarios para crear/actualizar
public class TaskRequest {
    @NotBlank(message = "El título no puede estar vacío")
    private String title;
    private String description;
    private TaskStatus status;
}

// Response DTO - Campos controlados para la respuesta
public class TaskResponse {
    private Long id;
    private String title;
    private String statusDisplayName;  // Campo calculado
    private LocalDateTime creationDate;
}
```

#### 3. Service Layer Pattern

**¿Qué es?** Capa dedicada para encapsular la lógica de negocio.

**¿Por qué se usó?**
- Separa las responsabilidades del controlador
- Permite reutilizar lógica desde múltiples endpoints
- Facilita el manejo de transacciones con `@Transactional`
- Mejora la testabilidad del código

```java
public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    List<TaskResponse> getAllTasks();
}

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
    // Implementación de la lógica de negocio
}
```

#### 4. Dependency Injection (Inyección de Dependencias)

**¿Qué es?** Patrón donde las dependencias se inyectan en lugar de ser creadas internamente.

**¿Por qué se usó?**
- Bajo acoplamiento entre componentes
- Facilita el testing con mocks
- Configuración centralizada por Spring
- Código más limpio y mantenible

```java
@RestController
public class TaskController {
    
    private final TaskService taskService;  // Inmutable
    
    // Constructor injection (preferido sobre @Autowired en campos)
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
}
```

**¿Por qué Constructor Injection sobre Field Injection?**

| Aspecto | Constructor Injection | @Autowired Field |
|---------|----------------------|------------------|
| Inmutabilidad | ✅ Campos `final` | ❌ Campos mutables |
| Testing | ✅ Fácil inyectar mocks | ❌ Requiere reflection |
| Dependencias | ✅ Explícitas | ❌ Ocultas |
| Detección de ciclos | ✅ Al inicio | ❌ En runtime |

#### 5. Global Exception Handler

**¿Qué es?** Manejador centralizado de excepciones usando `@RestControllerAdvice`.

**¿Por qué se usó?**
- Respuestas de error consistentes en toda la API
- Código más limpio en los controladores
- Logging centralizado de excepciones
- Facilita el mantenimiento

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(new ErrorResponse(...));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        // Manejo de errores de validación
    }
}
```

### Decisiones de Diseño

| Decisión | Alternativas Consideradas | Justificación |
|----------|--------------------------|---------------|
| **Spring Boot 4.0** | Quarkus, Micronaut | Ecosistema maduro, amplia documentación, soporte enterprise |
| **PostgreSQL** | MySQL, MongoDB | Robustez, soporte JSON, excelente rendimiento |
| **JPA/Hibernate** | JDBC Template, MyBatis | ORM maduro, reduce boilerplate, migrations automáticas |
| **SpringDoc OpenAPI** | Springfox | Activamente mantenido, soporte Spring Boot 3/4 |
| **Constructor Injection** | Field Injection | Inmutabilidad, testabilidad, dependencias explícitas |
| **H2 (dev) / PostgreSQL (prod)** | Solo PostgreSQL | Desarrollo rápido sin dependencias externas |

---

## 📁 Estructura del Proyecto

```
task-api/
├── 📄 pom.xml                              # Configuración Maven
├── 📄 azure-pipelines.yml                  # Pipeline CI/CD
├── 📄 README.md                            # Este archivo
│
├── 📂 src/main/java/com/tasks/.../
│   ├── 📄 Application.java                 # Clase principal @SpringBootApplication
│   │
│   ├── 📂 config/
│   │   └── 📄 OpenApiConfig.java           # Configuración Swagger/OpenAPI
│   │
│   ├── 📂 controller/
│   │   └── 📄 TaskController.java          # REST endpoints (@RestController)
│   │
│   ├── 📂 dto/
│   │   ├── 📄 ApiResponse.java             # Wrapper genérico de respuestas
│   │   ├── 📄 ErrorResponse.java           # Estructura de errores
│   │   ├── 📄 TaskRequest.java             # DTO de entrada con validaciones
│   │   └── 📄 TaskResponse.java            # DTO de salida
│   │
│   ├── 📂 entity/
│   │   └── 📄 Task.java                    # Entidad JPA (@Entity)
│   │
│   ├── 📂 enums/
│   │   └── 📄 TaskStatus.java              # Estados de tarea (PENDING, IN_PROGRESS, COMPLETED)
│   │
│   ├── 📂 exception/
│   │   ├── 📄 GlobalExceptionHandler.java  # Manejador global (@RestControllerAdvice)
│   │   └── 📄 ResourceNotFoundException.java
│   │
│   ├── 📂 repository/
│   │   └── 📄 TaskRepository.java          # Repositorio JPA (@Repository)
│   │
│   └── 📂 service/
│       ├── 📄 TaskService.java             # Interface del servicio
│       └── 📄 TaskServiceImpl.java         # Implementación (@Service)
│
├── 📂 src/main/resources/
│   └── 📄 application.properties           # Configuración de la aplicación
│
└── 📂 src/test/java/
    └── 📄 TaskServiceIntegrationTest.java  # Pruebas de integración
```

---

## 🔄 CI/CD Pipeline

El proyecto utiliza **Azure Pipelines** para integración y despliegue continuo.

### Flujo del Pipeline

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   COMMIT    │───▶│    BUILD    │───▶│   DEPLOY    │
│   (main)    │    │   & TEST    │    │   (Azure)   │
└─────────────┘    └─────────────┘    └─────────────┘
                         │
                         ▼
                   ┌─────────────┐
                   │  ARTIFACTS  │
                   │    (JAR)    │
                   └─────────────┘
```

### Stages del Pipeline

| Stage | Descripción | Trigger |
|-------|-------------|---------|
| **Build** | Compila código, ejecuta tests, genera JAR | Push a cualquier rama |
| **Deploy** | Despliega a Azure App Service | Solo rama `main` |

### Ver Pipeline

- **Azure DevOps Pipeline**: [https://dev.azure.com/sandoval-org/task-api/_build](https://dev.azure.com/sandoval-org/task-api/_build)

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 LTS | Lenguaje de programación |
| Spring Boot | 4.0.0 | Framework principal |
| Spring Data JPA | 4.0.x | Acceso a datos |
| Hibernate | 7.x | ORM |
| PostgreSQL | 17.x | Base de datos (producción) |
| H2 Database | 2.x | Base de datos (desarrollo) |
| SpringDoc OpenAPI | 2.8.0 | Documentación Swagger |
| Maven | 3.9+ | Gestión de dependencias |
| Azure App Service | - | Hosting |
| Azure Pipelines | - | CI/CD |

---

## 🧪 Ejecutar Pruebas

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar prueba específica
mvn test -Dtest=TaskServiceIntegrationTest

# Ejecutar con reporte de cobertura
mvn test jacoco:report
```

---

## 📝 Variables de Entorno

Para producción, configura estas variables:

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `DATABASE_URL` | URL de PostgreSQL | `jdbc:postgresql://host:5432/db` |
| `PGUSER` | Usuario de la BD | `postgres` |
| `PGPASSWORD` | Contraseña de la BD | `********` |
| `SERVER_PORT` | Puerto del servidor | `8080` |

---

## 👤 Autor

**Emmanuel Sandoval Morales**

- Azure DevOps: [sandoval-org/task-api](https://dev.azure.com/sandoval-org/task-api)
- API URL: [task-api-emmanuel](https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net)

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.

---

⭐ ¡Gracias por revisar este proyecto!