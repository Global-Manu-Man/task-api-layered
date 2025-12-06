package com.tasks.com.yaganaste.com.dto;


import com.tasks.com.yaganaste.com.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o actualizar una tarea")
public class TaskRequest {

    @Schema(description = "Título de la tarea", example = "Completar documentación", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, max = 200, message = "El título debe tener entre 3 y 200 caracteres")
    private String title;

    @Schema(description = "Descripción detallada de la tarea", example = "Escribir la documentación técnica del proyecto")
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    @Schema(description = "Estado de la tarea", example = "PENDING")
    private TaskStatus status;

    // Constructor por defecto
    public TaskRequest() {
    }

    // Constructor con parámetros
    public TaskRequest(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    // Getters y Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}

