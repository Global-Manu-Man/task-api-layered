package com.tasks.com.yaganaste.com.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador para la página de inicio y health check de la API.
 */
@RestController
public class HomeController {

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    /**
     * Endpoint principal - Información de la API
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home(HttpServletRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();
        
        // Obtener la URL base dinámicamente
        String baseUrl = getBaseUrl(request);
        
        response.put("application", "Task Management API");
        response.put("version", "1.0.0");
        response.put("status", "UP");
        response.put("message", "API REST para la gestión de tareas - Spring Boot 4.0");
        response.put("timestamp", LocalDateTime.now(ZoneId.of("America/Mexico_City")).toString());
        response.put("profile", activeProfile);
        
        // Links útiles con URLs completas
        Map<String, String> links = new LinkedHashMap<>();
        links.put("swagger_ui", baseUrl + "/swagger-ui.html");
        links.put("api_docs", baseUrl + "/api-docs");
        links.put("tasks", baseUrl + "/api/tasks");
        links.put("tasks_stats", baseUrl + "/api/tasks/stats");
        links.put("health", baseUrl + "/health");
        links.put("info", baseUrl + "/info");
        response.put("_links", links);
        
        // Endpoints disponibles
        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("GET /api/tasks", "Obtener todas las tareas");
        endpoints.put("GET /api/tasks/{id}", "Obtener tarea por ID");
        endpoints.put("GET /api/tasks/status/{status}", "Filtrar por estado (PENDING, IN_PROGRESS, COMPLETED)");
        endpoints.put("GET /api/tasks/search?keyword={texto}", "Buscar tareas por texto");
        endpoints.put("GET /api/tasks/stats", "Obtener estadísticas");
        endpoints.put("POST /api/tasks", "Crear nueva tarea");
        endpoints.put("PUT /api/tasks/{id}", "Actualizar tarea");
        endpoints.put("DELETE /api/tasks/{id}", "Eliminar tarea");
        response.put("endpoints", endpoints);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de health check para Azure y monitoreo
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new LinkedHashMap<>();
        
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now(ZoneId.of("America/Mexico_City")).toString());
        response.put("service", "task-api");
        response.put("profile", activeProfile);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de información para Azure health probes
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new LinkedHashMap<>();
        
        response.put("app", "Task Management API");
        response.put("version", "1.0.0");
        response.put("description", "API REST para la gestión de tareas");
        response.put("java", System.getProperty("java.version"));
        response.put("spring_boot", "4.0.0");
        response.put("profile", activeProfile);
        
        // Información del sistema
        Map<String, Object> system = new LinkedHashMap<>();
        system.put("os", System.getProperty("os.name"));
        system.put("timezone", ZoneId.systemDefault().toString());
        response.put("system", system);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene la URL base del servidor dinámicamente
     */
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        
        // En Azure, usar HTTPS y omitir el puerto
        if (serverName.contains("azurewebsites.net")) {
            return "https://" + serverName;
        }
        
        // Para desarrollo local
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        
        // Solo agregar puerto si no es el default
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        
        return url.toString();
    }
}