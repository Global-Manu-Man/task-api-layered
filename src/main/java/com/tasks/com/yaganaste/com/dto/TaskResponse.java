package com.tasks.com.yaganaste.com.dto;


import com.tasks.com.yaganaste.com.entity.*;
import com.tasks.com.yaganaste.com.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Datos de respuesta de una tarea")
public class TaskResponse {

    @Schema(description = "ID único de la tarea", example = "1")
    private Long id;

    @Schema(description = "Título de la tarea", example = "Completar documentación")
    private String title;

    @Schema(description = "Descripción de la tarea", example = "Escribir la documentación técnica")
    private String description;

    @Schema(description = "Estado actual de la tarea", example = "PENDING")
    private TaskStatus status;

    @Schema(description = "Nombre legible del estado", example = "Pendiente")
    private String statusDisplayName;

    @Schema(description = "Fecha de creación", example = "2024-12-05T10:30:00")
    private LocalDateTime creationDate;

    @Schema(description = "Fecha de última actualización", example = "2024-12-05T14:45:00")
    private LocalDateTime updatedDate;

    // Constructor por defecto
    public TaskResponse() {
    }

    // Constructor desde entidad
    public TaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.statusDisplayName = task.getStatus().getDisplayName();
        this.creationDate = task.getCreationDate();
        this.updatedDate = task.getUpdatedDate();
    }

    // Método estático para convertir desde entidad
    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(task);
    }

    // ========================================
    // GETTERS
    // ========================================
    
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getStatusDisplayName() {
        return statusDisplayName;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    // ========================================
    // SETTERS
    // ========================================
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setStatusDisplayName(String statusDisplayName) {
        this.statusDisplayName = statusDisplayName;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
