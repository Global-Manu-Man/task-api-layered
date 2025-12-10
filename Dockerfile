# ========================================
# STAGE 1: BUILD & TEST
# ========================================
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Paso 1: Copiar archivos de dependencias (aprovecha cache de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Paso 2: Copiar código fuente
COPY src ./src

# Paso 3: Ejecutar tests
RUN echo "=== Ejecutando Tests ===" && \
    mvn test

# Paso 4: Compilar y empaquetar (si los tests pasan)
RUN echo "=== Compilando Aplicación ===" && \
    mvn clean package -DskipTests

# ========================================
# STAGE 2: RUNTIME (imagen final liviana)
# ========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no-root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Paso 5: Copiar JAR al contenedor final
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Health check para monitoreo
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget -q --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]