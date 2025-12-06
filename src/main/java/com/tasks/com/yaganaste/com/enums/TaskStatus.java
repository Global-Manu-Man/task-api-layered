package com.tasks.com.yaganaste.com.enums;

/**
 * Enumeración que representa los posibles estados de una tarea.
 */
public enum TaskStatus {
    PENDING("Pendiente"),
    IN_PROGRESS("En Progreso"),
    COMPLETED("Completada");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}