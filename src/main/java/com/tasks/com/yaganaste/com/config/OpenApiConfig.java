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

/**
 * Configuración de OpenAPI/Swagger para documentación de la API.
 * Configura servidores dinámicamente según el perfil activo.
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getApiInfo())
                .servers(getServers());
    }

    private Info getApiInfo() {
        return new Info()
                .title("Task Management API")
                .version("1.0.0")
                .description("""
                    API REST para la gestión de tareas.
                    
                    **Características:**
                    - CRUD completo de tareas
                    - Filtrado por estado
                    - Búsqueda por texto
                    - Estadísticas
                    
                    **Estados de Tarea:**
                    - **PENDING**: Tarea pendiente de iniciar
                    - **IN_PROGRESS**: Tarea en progreso
                    - **COMPLETED**: Tarea completada
                    """)
                .contact(new Contact()
                        .name("API Support")
                        .email("emmanuel.sandoval@outlook.com")
                        .url("https://github.com/emmanuel-sandoval"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private List<Server> getServers() {
        if ("prod".equalsIgnoreCase(activeProfile)) {
            // Servidor de producción (Azure)
            Server prodServer = new Server()
                    .url("https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net")
                    .description("Servidor de producción (Azure)");
            
            return List.of(prodServer);
        } else {
            // Servidor de desarrollo (localhost)
            Server devServer = new Server()
                    .url("http://localhost:8080")
                    .description("Servidor de desarrollo");
            
            // También agregar producción como opción en desarrollo
            Server prodServer = new Server()
                    .url("https://task-api-emmanuel-fqdegpgedaemcxc2.centralus-01.azurewebsites.net")
                    .description("Servidor de producción (Azure)");
            
            return List.of(devServer, prodServer);
        }
    }
}