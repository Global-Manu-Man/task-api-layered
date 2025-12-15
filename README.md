# 📋 Task Management API

API REST para la gestión de tareas desarrollada con **Spring Boot 4.0** y **Java 17**.

[![Build Status](https://img.shields.io/badge/Coolify-Deployed-brightgreen)](https://tasks.pakal.solutions)
[![Grafana](https://img.shields.io/badge/Grafana-Monitoring-orange)](https://grafana.pakal.solutions)
[![Prometheus](https://img.shields.io/badge/Prometheus-Metrics-red)](https://prometheus.pakal.solutions)

---

## 🌐 Demo en Vivo

| Recurso | URL |
|---------|-----|
| **API Base** | [https://tasks.pakal.solutions](https://tasks.pakal.solutions) |
| **Swagger UI** | [https://tasks.pakal.solutions/swagger-ui.html](https://tasks.pakal.solutions/swagger-ui.html) |
| **OpenAPI Spec** | [https://tasks.pakal.solutions/api-docs](https://tasks.pakal.solutions/api-docs) |
| **Health Check** | [https://tasks.pakal.solutions/actuator/health](https://tasks.pakal.solutions/actuator/health) |
| **Métricas Prometheus** | [https://tasks.pakal.solutions/actuator/prometheus](https://tasks.pakal.solutions/actuator/prometheus) |
| **Grafana Dashboard** | [https://grafana.pakal.solutions](https://grafana.pakal.solutions) |
| **Prometheus** | [https://prometheus.pakal.solutions](https://prometheus.pakal.solutions) |
| **GitHub Repository** | [https://github.com/tu-usuario/task-api](https://github.com/tu-usuario/task-api) |

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
11. [Infraestructura y Hosting](#-infraestructura-y-hosting)
12. [Monitoreo y Observabilidad](#-monitoreo-y-observabilidad)
13. [Alertas](#-alertas)
14. [Tecnologías Utilizadas](#-tecnologías-utilizadas)

---

## 📥 Clonar el Proyecto

### Opción 1: HTTPS
```bash
git clone https://github.com/tu-usuario/task-api.git
```

### Opción 2: SSH
```bash
git clone git@github.com:tu-usuario/task-api.git
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
| **Docker** | 20.x+ | `docker --version` |

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

# Perfil de producción (PostgreSQL)
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Opción 3: Con el JAR
```bash
# Desarrollo (H2)
java -jar target/yaganaste.com-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Producción (PostgreSQL)
java -jar target/yaganaste.com-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Opción 4: Con Docker
```bash
# Construir imagen
docker build -t task-api .

# Ejecutar contenedor
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/db \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=pass \
  task-api
```

### Verificar que está corriendo

Una vez iniciada la aplicación, verifica que funciona:

```bash
# Health check
curl http://localhost:8080/actuator/health

# O abre en el navegador
http://localhost:8080/swagger-ui.html
```

### URLs Locales Disponibles

| Recurso | URL |
|---------|-----|
| API Tasks | http://localhost:8080/api/tasks |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/api-docs |
| Health Check | http://localhost:8080/actuator/health |
| Métricas Prometheus | http://localhost:8080/actuator/prometheus |
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
   │  Local  │    │  Coolify │    │  Tests  │
   │  (IDE)  │    │ (Hetzner)│    │  (mvn)  │
   └────┬────┘    └────┬─────┘    └────┬────┘
        │              │               │
        ▼              ▼               ▼
   ┌─────────┐   ┌──────────┐    ┌─────────┐
   │   dev   │   │   prod   │    │  test   │
   └────┬────┘   └────┬─────┘    └────┬────┘
        │              │               │
        ▼              ▼               ▼
   ┌─────────┐   ┌──────────┐    ┌─────────┐
   │   H2    │   │PostgreSQL│    │   H2    │
   │ memoria │   │ Railway  │    │ memoria │
   └─────────┘   └──────────┘    └─────────┘
```

### Archivos de Configuración

```
src/main/resources/
├── application.properties        # Configuración principal (selección de perfil)
├── application-dev.properties    # Desarrollo local (H2)
├── application-prod.properties   # Producción (PostgreSQL Railway)
└── application-test.properties   # Testing (H2)
```

---

### 📄 application-prod.properties (Producción)

```properties
# =====================================================
# TASK API - PRODUCCIÓN (PostgreSQL - Railway)
# =====================================================

# ========== POSTGRESQL CONNECTION ==========
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# ========== JPA / HIBERNATE ==========
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# ========== CONNECTION POOL (HikariCP) ==========
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.idle-timeout=30000

# ========== ACTUATOR + PROMETHEUS METRICS ==========
management.endpoints.web.exposure.include=health,info,metrics,prometheus
management.endpoint.health.show-details=always
management.metrics.export.prometheus.enabled=true
management.metrics.tags.application=task-api
management.metrics.tags.environment=production

# ========== SWAGGER ==========
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html

# ========== LOGGING ==========
logging.level.root=INFO
logging.level.com.tasks=INFO
```

---

## 🗄️ Base de Datos

### PostgreSQL (Producción - Railway)

El proyecto utiliza **PostgreSQL** alojado en **Railway** para el ambiente de producción.

| Configuración | Valor |
|---------------|-------|
| **Proveedor** | Railway |
| **Driver** | PostgreSQL |
| **Puerto** | 5432 |
| **Dialect** | PostgreSQLDialect |

### Variables de Entorno para Producción

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://host:port/database
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_PROFILES_ACTIVE=prod
```

### H2 Database (Desarrollo y Tests)

Para desarrollo local se usa H2 en memoria:

| Campo | Valor |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:taskdb` |
| Username | `sa` |
| Password | (vacío) |
| Console | http://localhost:8080/h2-console |

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

### Endpoints de Monitoreo

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/actuator/health` | Health check |
| `GET` | `/actuator/info` | Información de la app |
| `GET` | `/actuator/prometheus` | Métricas para Prometheus |
| `GET` | `/actuator/metrics` | Métricas generales |

### Estados Disponibles

| Estado | Descripción |
|--------|-------------|
| `PENDING` | Tarea pendiente de iniciar |
| `IN_PROGRESS` | Tarea en progreso |
| `COMPLETED` | Tarea completada |

### Ejemplos de Uso

**Crear una tarea:**
```bash
curl -X POST https://tasks.pakal.solutions/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Mi primera tarea",
    "description": "Descripción de la tarea",
    "status": "PENDING"
  }'
```

**Obtener todas las tareas:**
```bash
curl https://tasks.pakal.solutions/api/tasks
```

**Health check:**
```bash
curl https://tasks.pakal.solutions/actuator/health
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
├── 📄 Dockerfile                           # Imagen Docker para Coolify
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
│   ├── 📄 application-prod.properties      # Producción (PostgreSQL)
│   └── 📄 application-test.properties      # Testing (H2)
│
├── 📂 prometheus/
│   └── 📄 prometheus.yml                   # Configuración de Prometheus
│
├── 📂 grafana/
│   └── 📂 provisioning/
│       ├── 📂 datasources/
│       │   └── 📄 datasource.yml           # Conexión a Prometheus
│       └── 📂 dashboards/
│           ├── 📄 dashboards.yml
│           └── 📄 spring-boot-dashboard.json
│
└── 📂 src/test/java/
    ├── 📄 TaskControllerTest.java          # Tests unitarios
    └── 📄 TaskControllerIntegrationTest.java # Tests de integración
```

---

## 🔄 CI/CD Pipeline

El proyecto migró de **Azure DevOps** a **GitHub + Coolify** para CI/CD.

### Pipeline Anterior (Azure DevOps)

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   COMMIT    │───▶│    BUILD    │───▶│   DEPLOY    │
│   (Azure)   │    │   & TEST    │    │   (Azure)   │
└─────────────┘    └─────────────┘    └─────────────┘
```

### Pipeline Actual (GitHub + Coolify)

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  GIT PUSH   │───▶│   COOLIFY   │───▶│   HETZNER   │
│  (GitHub)   │    │  Dockerfile │    │    VPS      │
└─────────────┘    └─────────────┘    └─────────────┘
       │                  │                   │
       │                  ▼                   │
       │         ┌─────────────┐              │
       │         │ Build & Test│              │
       │         │  (Docker)   │              │
       │         └─────────────┘              │
       │                  │                   │
       │                  ▼                   │
       └────────▶ Auto-deploy on push ◀───────┘
```

### Dockerfile

```dockerfile
# ========================================
# STAGE 1: BUILD & TEST
# ========================================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

# Ejecutar tests
RUN mvn test

# Compilar
RUN mvn clean package -DskipTests

# ========================================
# STAGE 2: RUNTIME
# ========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🏠 Infraestructura y Hosting

### Arquitectura de Infraestructura

```
┌─────────────────────────────────────────────────────────────────┐
│                         INTERNET                                 │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                     CLOUDFLARE DNS                               │
│  ┌─────────────────────────────────────────────────────────┐    │
│  │  *.pakal.solutions  →  5.78.90.187 (Hetzner VPS)        │    │
│  └─────────────────────────────────────────────────────────┘    │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    HETZNER VPS (CCX13)                          │
│                   Hillsboro, OR (US West)                       │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  2 vCPU │ 8 GB RAM │ 80 GB SSD │ $14.49/mo               │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                    COOLIFY                                 │  │
│  │  ┌─────────────┐  ┌────────────┐  ┌─────────────┐        │  │
│  │  │  Task API   │  │ Prometheus │  │   Grafana   │        │  │
│  │  │   :8080     │  │   :9090    │  │   :3000     │        │  │
│  │  └─────────────┘  └────────────┘  └─────────────┘        │  │
│  │                          │                                │  │
│  │                    TRAEFIK (SSL)                          │  │
│  │                  Let's Encrypt                            │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                    RAILWAY (PostgreSQL)                          │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │  PostgreSQL Database                                       │  │
│  └───────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### Servicios y URLs

| Servicio | URL | Puerto Interno |
|----------|-----|----------------|
| **Task API** | https://tasks.pakal.solutions | 8080 |
| **Prometheus** | https://prometheus.pakal.solutions | 9090 |
| **Grafana** | https://grafana.pakal.solutions | 3000 |
| **Swagger UI** | https://tasks.pakal.solutions/swagger-ui.html | 8080 |

### Configuración DNS (Cloudflare)

| Type | Name | Content | Proxy |
|------|------|---------|-------|
| A | * | 5.78.90.187 | DNS only |
| A | pakal.solutions | 5.78.90.187 | DNS only |

> ⚠️ **Importante:** El proxy de Cloudflare debe estar desactivado (DNS only) para que Coolify/Traefik genere los certificados SSL con Let's Encrypt.

---

## 📊 Monitoreo y Observabilidad

### Stack de Monitoreo

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Task API   │────▶│ Prometheus  │────▶│   Grafana   │
│  /actuator/ │     │  scrape     │     │  dashboard  │
│  prometheus │     │  cada 5s    │     │             │
└─────────────┘     └─────────────┘     └─────────────┘
```

### Métricas Disponibles

| Categoría | Métricas |
|-----------|----------|
| **JVM** | Heap Memory, Non-Heap, GC, Threads |
| **HTTP** | Request Count, Response Time, Status Codes |
| **HikariCP** | Active Connections, Idle, Pending |
| **System** | CPU Usage, Load Average |
| **Logback** | INFO, WARN, ERROR, DEBUG counts |

### Prometheus Configuration

```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'task-api'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
      - targets: ['tasks.pakal.solutions:443']
    scheme: https
    tls_config:
      insecure_skip_verify: true
```

### Grafana Dashboard

Se utiliza el dashboard **Spring Boot APM** (ID: 12900) que incluye:

- Basic Statistics (Uptime, Heap Used, Non-Heap)
- CPU Usage & Load Average
- JVM Memory (Eden, Old Gen, Survivor Space)
- HikariCP Connection Pool
- HTTP Request Statistics
- Logback Statistics

**Acceso a Grafana:**
- URL: https://grafana.pakal.solutions
- Usuario: admin
- Password: (configurado en variables de entorno)

---

## 🚨 Alertas

### Configuración de Alertas con Telegram

El sistema está configurado para enviar alertas a Telegram cuando:
- El servicio Task API se cae
- Uso de memoria supera el 80%
- Errores HTTP 5xx aumentan

### Configuración del Bot de Telegram

1. Crear bot con @BotFather
2. Obtener token del bot
3. Obtener Chat ID
4. Configurar en Grafana → Alerting → Contact Points

### Reglas de Alerta Configuradas

| Alerta | Condición | Severidad |
|--------|-----------|-----------|
| Task API Down | `up{job="task-api"} == 0` | Critical |
| High Memory Usage | `jvm_memory_used > 80%` | Warning |
| HTTP 5xx Errors | `rate(http_5xx) > 0.1` | Warning |

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 LTS | Lenguaje de programación |
| Spring Boot | 4.0.0 | Framework principal |
| Spring Data JPA | 4.0.x | Acceso a datos |
| Hibernate | 7.x | ORM |
| **PostgreSQL** | 15.x | **Base de datos (producción)** |
| H2 Database | 2.x | Base de datos (desarrollo/tests) |
| SpringDoc OpenAPI | 2.8.0 | Documentación Swagger |
| Micrometer | 1.x | Métricas y observabilidad |
| HikariCP | 7.x | Connection Pool |
| Maven | 3.9+ | Gestión de dependencias |
| Docker | 20.x+ | Contenedorización |
| **Coolify** | 4.x | **Plataforma de deployment** |
| **Hetzner** | CCX13 | **VPS Hosting** |
| **Prometheus** | latest | **Recolección de métricas** |
| **Grafana** | latest | **Visualización y dashboards** |
| **Cloudflare** | - | **DNS y protección** |
| **Railway** | - | **PostgreSQL hosting** |
| **Telegram** | - | **Alertas y notificaciones** |

---

## 🔄 Migración Realizada

### De Azure a Self-Hosted

| Componente | Antes | Después |
|------------|-------|---------|
| **Repositorio** | Azure DevOps | GitHub |
| **CI/CD** | Azure Pipelines | Coolify + Docker |
| **Hosting** | Azure App Service | Hetzner VPS |
| **Base de datos** | Azure SQL Server | PostgreSQL (Railway) |
| **SSL** | Azure managed | Let's Encrypt (Traefik) |
| **Monitoreo** | Application Insights | Prometheus + Grafana |
| **Alertas** | Azure Alerts | Grafana + Telegram |
| **DNS** | Azure DNS | Cloudflare |

### Beneficios de la Migración

| Aspecto | Beneficio |
|---------|-----------|
| **Costo** | ~$15/mes vs ~$50+/mes en Azure |
| **Control** | Full control sobre la infraestructura |
| **Flexibilidad** | Fácil agregar más servicios |
| **Monitoreo** | Dashboards personalizados con Grafana |
| **Deployment** | Auto-deploy en cada push a main |

---

## 🧪 Ejecutar Pruebas

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar prueba específica
mvn test -Dtest=TaskControllerTest

# Ejecutar tests de integración
mvn test -Dtest=TaskControllerIntegrationTest

# Ver reporte de cobertura
mvn jacoco:report
```

---

## 🔐 Consideraciones de Seguridad

### Variables de Entorno (nunca en código)

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
SPRING_PROFILES_ACTIVE=prod
GF_SECURITY_ADMIN_PASSWORD=...
```

### ddl-auto por Ambiente

| Ambiente | Valor | Razón |
|----------|-------|-------|
| **Desarrollo** | `create-drop` | Reinicia BD en cada ejecución |
| **Testing** | `create-drop` | Tests con BD limpia |
| **Producción** | `update` | Solo actualiza schema |

---

## 👤 Autor

**Emmanuel Sandoval Morales**

- GitHub: [tu-usuario](https://github.com/tu-usuario)
- Portfolio: [pakal.solutions](https://pakal.solutions)
- API URL: [tasks.pakal.solutions](https://tasks.pakal.solutions)

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT.

---

⭐ ¡Gracias por revisar este proyecto!
