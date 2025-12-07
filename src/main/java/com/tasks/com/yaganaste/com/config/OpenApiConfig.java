package com.tasks.com.yaganaste.com.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Management API")
                        .version("1.0.0")
                        .description("""
                                API REST para la gestión de tareas.
                                
                                ## Características
                                - CRUD completo de tareas
                                - Filtrado por estado
                                - Búsqueda por texto
                                - Estadísticas
                                
                                ## Estados de Tarea
                                - **PENDING**: Tarea pendiente de iniciar
                                - **IN_PROGRESS**: Tarea en progreso
                                - **COMPLETED**: Tarea completada
                                """)
                        .contact(new Contact()
                                .name("API Support")
                                .email("sandoval.morales.emmanuel@outlook.com")
                                .url("https://emmanuel.pakal.cloud"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de desarrollo")
                ));
    }
}

