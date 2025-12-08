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
| **Azure SpringDoc** | [https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net/](https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net/swagger-ui/index.html) |
---

## 📖 Tabla de Contenidos

1. [Clonar el Proyecto](#-clonar-el-proyecto)
2. [Requisitos Previos](#-requisitos-previos)
3. [Construir el Proyecto](#-construir-el-proyecto)
4. [Ejecutar el Proyecto](#-ejecutar-el-proyecto)
5. [Configuración por Ambientes](#-configuración-por-ambientes)
6. [Base de Datos](#-base-de-datos)
7. [Endpoints de la API](#-endpoints-de-la-api)
8. [Arquitectura y Patrones de Diseño](#-arquitectura-y-patrones-de-diseño)
9. [Estructura del Proyecto](#-estructura-del-proyecto)
10. [CI/CD Pipeline](#-cicd-pipeline)
11. [Tecnologías Utilizadas](#-tecnologías-utilizadas)

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

El archivo JAR se genera en: `target/yaganaste.com-0.0.1-SNAPSHOT.jar`

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

### Opción 2: Con Maven y perfil específico
```bash
# Perfil de desarrollo (H2)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Perfil de producción (SQL Server)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Opción 3: Con el JAR
```bash
# Desarrollo (H2)
java -jar target/yaganaste.com-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Producción (SQL Server)
java -jar target/yaganaste.com-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
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
| H2 Console (solo dev) | http://localhost:8080/h2-console |

---

## ⚙️ Configuración por Ambientes

El proyecto utiliza **Spring Profiles** para manejar diferentes configuraciones según el ambiente.

### Diagrama de Selección de Perfil

```
┌─────────────────────────────────────────────────┐
│              ¿Dónde se ejecuta?                 │
└───────────────────────┬─────────────────────────┘
                        │
        ┌───────────────┼───────────────┐
        ▼               ▼               ▼
   ┌─────────┐    ┌──────────┐    ┌─────────┐
   │  Local  │    │  Azure   │    │  Tests  │
   │  (IDE)  │    │App Service│   │  (mvn)  │
   └────┬────┘    └────┬─────┘    └────┬────┘
        │              │               │
        ▼              ▼               ▼
   ┌─────────┐   ┌──────────┐    ┌─────────┐
   │   dev   │   │   prod   │    │  test   │
   └────┬────┘   └────┬─────┘    └────┬────┘
        │              │               │
        ▼              ▼               ▼
   ┌─────────┐   ┌──────────┐    ┌─────────┐
   │   H2    │   │SQL Server│    │   H2    │
   │ memoria │   │  Azure   │    │ memoria │
   └─────────┘   └──────────┘    └─────────┘
```

### Archivos de Configuración

```
src/main/resources/
├── application.properties        # Configuración principal (selección de perfil)
├── application-dev.properties    # Desarrollo local (H2)
├── application-prod.properties   # Producción (Azure SQL Server)
└── application-test.properties   # Testing (H2)
```

---

### 📄 application.properties (Principal)

```properties
# =====================================================
# TASK API - CONFIGURACIÓN PRINCIPAL
# Spring Boot 4.0
# =====================================================

# ========== PERFIL ACTIVO ==========
# - dev: H2 en memoria (desarrollo local)
# - prod: SQL Server en Azure (producción)
# - test: H2 en memoria (pruebas)
spring.profiles.active=${SPRING_PROFILES_ACTIVE:dev}

# ========== APLICACIÓN ==========
spring.application.name=task-api
server.port=${PORT:8080}

# ========== SWAGGER / OPENAPI ==========
springdoc.api-docs.enabled=true
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method

# ========== JACKSON (JSON) ==========
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss
```

---

### 📄 application-dev.properties (Desarrollo)

```properties
# =====================================================
# TASK API - DESARROLLO LOCAL (H2 Database)
# Activar con: spring.profiles.active=dev
# =====================================================

# ========== H2 DATABASE (En memoria) ==========
spring.datasource.url=jdbc:h2:mem:taskdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

# ========== JPA / HIBERNATE ==========
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# ========== H2 CONSOLE ==========
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# ========== SWAGGER ==========
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true

# ========== LOGGING (Detallado) ==========
logging.level.root=INFO
logging.level.com.tasks=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

### 📄 application-prod.properties (Producción - Azure SQL Server)

```properties
# =====================================================
# TASK API - PRODUCCIÓN (Azure SQL Server)
# Activar con: SPRING_PROFILES_ACTIVE=prod
# =====================================================

# ========== SQL SERVER CONNECTION ==========
spring.datasource.url=jdbc:sqlserver://${DB_SERVER}.database.windows.net:1433;database=${DB_NAME};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# ========== JPA / HIBERNATE ==========
# IMPORTANTE: En producción usar 'none' o 'validate', NUNCA 'update' o 'create'
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false

# ========== CONNECTION POOL (HikariCP) ==========
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.max-lifetime=1800000

# ========== SWAGGER ==========
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true

# ========== MANEJO DE RECURSOS NO ENCONTRADOS ==========
spring.web.resources.add-mappings=false
spring.mvc.throw-exception-if-no-handler-found=true

# ========== LOGGING ==========
logging.level.root=INFO
logging.level.com.tasks=INFO
logging.level.org.hibernate.SQL=WARN
logging.level.org.springframework.web=WARN
```

---

### 📄 application-test.properties (Testing)

```properties
# =====================================================
# TASK API - TESTING (H2 Database)
# Activar con: spring.profiles.active=test
# =====================================================

# ========== H2 DATABASE (En memoria para tests) ==========
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

# ========== JPA / HIBERNATE ==========
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

# ========== H2 CONSOLE (Deshabilitado en tests) ==========
spring.h2.console.enabled=false

# ========== SWAGGER (Deshabilitado en tests) ==========
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false

# ========== LOGGING (Mínimo para tests) ==========
logging.level.root=WARN
logging.level.com.tasks=INFO
logging.level.org.hibernate.SQL=WARN
```

---

### Resumen de Configuración por Perfil

| Configuración | dev | prod | test |
|---------------|-----|------|------|
| **Base de datos** | H2 (memoria) | Azure SQL Server | H2 (memoria) |
| **ddl-auto** | `create-drop` | `none` | `create-drop` |
| **H2 Console** | ✅ Habilitado | ❌ N/A | ❌ Deshabilitado |
| **Swagger** | ✅ Habilitado | ✅ Habilitado | ❌ Deshabilitado |
| **SQL Logging** | DEBUG | WARN | WARN |

---

## 🗄️ Base de Datos

### Arquitectura de Base de Datos

```
┌─────────────────────────────────────────────────────────────┐
│                      Tu Código Java                          │
│        (Entity, Repository, Service, Controller)            │
└─────────────────────────┬───────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────┐
│                     JPA / Hibernate                          │
│              (Abstracción de Base de Datos)                 │
└─────────────────────────┬───────────────────────────────────┘
                          │
          ┌───────────────┼───────────────┐
          ▼               ▼               ▼
    ┌──────────┐   ┌──────────┐    ┌──────────┐
    │    H2    │   │SQL Server│    │PostgreSQL│
    │  (Dev)   │   │  (Prod)  │    │ (Futuro) │
    └──────────┘   └──────────┘    └──────────┘
```

### Azure SQL Server (Producción)

La aplicación en producción utiliza **Azure SQL Database**.

#### Variables de Entorno en Azure App Service

| Variable | Descripción | Ejemplo |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Perfil activo | `prod` |
| `DB_SERVER` | Nombre del servidor SQL | `task-api-sql-server` |
| `DB_NAME` | Nombre de la base de datos | `taskdb` |
| `DB_USERNAME` | Usuario de la BD | `sqladmin` |
| `DB_PASSWORD` | Contraseña de la BD | `********` |

#### Script de Creación de Tabla (SQL Server)

```sql
-- ========== CREAR TABLA TASKS ==========
CREATE TABLE tasks (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    description NVARCHAR(MAX) NULL,
    status NVARCHAR(50) NOT NULL DEFAULT 'PENDING',
    creation_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    updated_date DATETIME2 NOT NULL DEFAULT GETDATE(),
    
    CONSTRAINT CHK_tasks_status CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED'))
);
GO

-- ========== ÍNDICES ==========
CREATE INDEX IX_tasks_status ON tasks(status);
CREATE INDEX IX_tasks_creation_date ON tasks(creation_date DESC);
GO

-- ========== DATOS DE PRUEBA ==========
INSERT INTO tasks (title, description, status, creation_date, updated_date) VALUES
('Revisar documentación del proyecto', 'Leer y analizar la documentación técnica existente', 'PENDING', GETDATE(), GETDATE()),
('Configurar entorno de desarrollo', 'Instalar JDK 17, Maven y configurar IDE', 'COMPLETED', GETDATE(), GETDATE()),
('Implementar autenticación JWT', 'Agregar seguridad con tokens JWT al API', 'IN_PROGRESS', GETDATE(), GETDATE()),
('Escribir pruebas unitarias', 'Crear tests para los servicios principales', 'PENDING', GETDATE(), GETDATE()),
('Desplegar en servidor de pruebas', 'Realizar deployment en ambiente de QA', 'PENDING', GETDATE(), GETDATE());
GO
```

### H2 Database (Desarrollo y Tests)

Para desarrollo local se usa H2 en memoria, lo que permite:
- Desarrollo rápido sin dependencias externas
- Cada reinicio comienza con una base de datos limpia
- Consola web para inspeccionar datos: http://localhost:8080/h2-console

#### Configuración de H2 Console

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:taskdb` |
| Username | `sa` |
| Password | (vacío) |

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

**Filtrar por estado:**
```bash
curl http://localhost:8080/api/tasks/status/PENDING
```

**Buscar tareas:**
```bash
curl "http://localhost:8080/api/tasks/search?q=documentación"
```

**Obtener estadísticas:**
```bash
curl http://localhost:8080/api/tasks/stats
```

---

## 🏗️ Arquitectura y Patrones de Diseño

### Arquitectura en Capas (Layered Architecture)

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

| Patrón | Descripción | Beneficio |
|--------|-------------|-----------|
| **Repository** | Abstrae acceso a datos | Desacoplamiento, testabilidad |
| **DTO** | Objetos de transferencia | Control de datos expuestos |
| **Service Layer** | Lógica de negocio centralizada | Reutilización, transacciones |
| **Dependency Injection** | Inyección por constructor | Inmutabilidad, testing |
| **Global Exception Handler** | Manejo centralizado de errores | Respuestas consistentes |

---

## 📁 Estructura del Proyecto

```
task-api/
├── 📄 pom.xml                              # Configuración Maven
├── 📄 azure-pipelines.yml                  # Pipeline CI/CD
├── 📄 README.md                            # Este archivo
│
├── 📂 src/main/java/com/tasks/.../
│   ├── 📄 Application.java                 # Clase principal
│   ├── 📂 config/                          # Configuraciones
│   ├── 📂 controller/                      # REST Controllers
│   ├── 📂 dto/                             # Data Transfer Objects
│   ├── 📂 entity/                          # Entidades JPA
│   ├── 📂 enums/                           # Enumeraciones
│   ├── 📂 exception/                       # Manejo de excepciones
│   ├── 📂 repository/                      # Repositorios JPA
│   └── 📂 service/                         # Servicios de negocio
│
├── 📂 src/main/resources/
│   ├── 📄 application.properties           # Configuración principal
│   ├── 📄 application-dev.properties       # Desarrollo (H2)
│   ├── 📄 application-prod.properties      # Producción (SQL Server)
│   └── 📄 application-test.properties      # Testing (H2)
│
└── 📂 src/test/java/
    ├── 📄 TaskControllerTest.java          # Tests unitarios
    └── 📄 TaskControllerIntegrationTest.java # Tests de integración
```

---

## 🔄 CI/CD Pipeline

El proyecto utiliza **Azure Pipelines** para integración y despliegue continuo.

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   COMMIT    │───▶│    BUILD    │───▶│   DEPLOY    │
│   (main)    │    │   & TEST    │    │   (Azure)   │
└─────────────┘    └─────────────┘    └─────────────┘
```

- **Azure DevOps Pipeline**: [https://dev.azure.com/sandoval-org/task-api/_build](https://dev.azure.com/sandoval-org/task-api/_build)

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 LTS | Lenguaje de programación |
| Spring Boot | 4.0.0 | Framework principal |
| Spring Data JPA | 4.0.x | Acceso a datos |
| Hibernate | 7.x | ORM |
| **Azure SQL Server** | 17.x | **Base de datos (producción)** |
| H2 Database | 2.x | Base de datos (desarrollo/tests) |
| SpringDoc OpenAPI | 2.8.0 | Documentación Swagger |
| HikariCP | 7.x | Connection Pool |
| Maven | 3.9+ | Gestión de dependencias |
| Azure App Service | - | Hosting |
| Azure Pipelines | - | CI/CD |

---

## 🧪 Ejecutar Pruebas

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar prueba específica
mvn test -Dtest=TaskControllerTest

# Ejecutar tests de integración
mvn test -Dtest=TaskControllerIntegrationTest
```

---

## 🔐 Consideraciones de Seguridad

### ddl-auto por Ambiente

| Ambiente | Valor | Razón |
|----------|-------|-------|
| **Desarrollo** | `create-drop` | Reinicia BD en cada ejecución |
| **Testing** | `create-drop` | Tests con BD limpia |
| **Producción** | `none` | ⚠️ NUNCA modificar schema automáticamente |

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