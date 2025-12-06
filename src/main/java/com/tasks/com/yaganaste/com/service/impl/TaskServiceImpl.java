package com.tasks.com.yaganaste.com.service.impl;

import com.tasks.com.yaganaste.com.dto.TaskRequest;
import com.tasks.com.yaganaste.com.dto.TaskResponse;
import com.tasks.com.yaganaste.com.entity.Task;
import com.tasks.com.yaganaste.com.enums.TaskStatus;
import com.tasks.com.yaganaste.com.exception.ResourceNotFoundException;
import com.tasks.com.yaganaste.com.repository.TaskRepository;
import com.tasks.com.yaganaste.com.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    // Inyección de dependencias por constructor
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {
        logger.info("Creando nueva tarea: {}", request.getTitle());
        
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING);
        
        Task savedTask = taskRepository.save(task);
        logger.info("Tarea creada con ID: {}", savedTask.getId());
        
        return TaskResponse.fromEntity(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        logger.info("Obteniendo todas las tareas");
        
        return taskRepository.findAll()
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        logger.info("Buscando tarea con ID: {}", id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea", "id", id));
        
        return TaskResponse.fromEntity(task);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        logger.info("Actualizando tarea con ID: {}", id);
        
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea", "id", id));
        
        // Actualizar campos
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        
        Task updatedTask = taskRepository.save(task);
        logger.info("Tarea actualizada: {}", updatedTask.getId());
        
        return TaskResponse.fromEntity(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        logger.info("Eliminando tarea con ID: {}", id);
        
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarea", "id", id);
        }
        
        taskRepository.deleteById(id);
        logger.info("Tarea eliminada exitosamente: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        logger.info("Buscando tareas con estado: {}", status);
        
        return taskRepository.findByStatus(status)
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> searchTasks(String searchTerm) {
        logger.info("Buscando tareas con término: {}", searchTerm);
        
        return taskRepository.searchByTitleOrDescription(searchTerm)
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long countTasksByStatus(TaskStatus status) {
        logger.info("Contando tareas con estado: {}", status);
        return taskRepository.countByStatus(status);
    }
}

