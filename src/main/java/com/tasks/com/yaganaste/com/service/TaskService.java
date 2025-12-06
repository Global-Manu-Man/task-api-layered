package com.tasks.com.yaganaste.com.service;

import com.tasks.com.yaganaste.com.dto.*;
import com.tasks.com.yaganaste.com.enums.*;
import java.util.List;


public interface TaskService {

    /**
     * Crea una nueva tarea.
     * @param request Datos de la tarea a crear
     * @return Tarea creada
     */
    TaskResponse createTask(TaskRequest request);

    /**
     * Obtiene todas las tareas.
     * @return Lista de todas las tareas
     */
    List<TaskResponse> getAllTasks();

    /**
     * Obtiene una tarea por su ID.
     * @param id ID de la tarea
     * @return Tarea encontrada
     */
    TaskResponse getTaskById(Long id);

    /**
     * Actualiza una tarea existente.
     * @param id ID de la tarea a actualizar
     * @param request Nuevos datos de la tarea
     * @return Tarea actualizada
     */
    TaskResponse updateTask(Long id, TaskRequest request);

    /**
     * Elimina una tarea.
     * @param id ID de la tarea a eliminar
     */
    void deleteTask(Long id);

    /**
     * Obtiene tareas por estado.
     * @param status Estado de las tareas
     * @return Lista de tareas con el estado especificado
     */
    List<TaskResponse> getTasksByStatus(TaskStatus status);

    /**
     * Busca tareas por término de búsqueda.
     * @param searchTerm Término a buscar
     * @return Lista de tareas que coinciden
     */
    List<TaskResponse> searchTasks(String searchTerm);

    /**
     * Cuenta tareas por estado.
     * @param status Estado de las tareas
     * @return Número de tareas
     */
    long countTasksByStatus(TaskStatus status);
}

