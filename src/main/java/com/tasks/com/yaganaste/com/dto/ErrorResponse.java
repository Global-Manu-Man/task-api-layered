package com.tasks.com.yaganaste.com.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "Respuesta de error de la API")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @Schema(description = "Timestamp del error")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "404")
    private int status;

    @Schema(description = "Tipo de error", example = "Not Found")
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "Tarea no encontrada con id: 1")
    private String message;

    @Schema(description = "Ruta del endpoint", example = "/api/tasks/1")
    private String path;

    @Schema(description = "Errores de validación por campo")
    private Map<String, String> validationErrors;

    @Schema(description = "Lista de errores detallados")
    private List<String> details;

    // Constructor básico
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Constructor con errores de validación
    public ErrorResponse(int status, String error, String message, String path, 
                         Map<String, String> validationErrors) {
        this(status, error, message, path);
        this.validationErrors = validationErrors;
    }

    // Getters y Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}


