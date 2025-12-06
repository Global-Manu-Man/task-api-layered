package com.tasks.com.yaganaste.com.controller;

import com.tasks.com.yaganaste.com.dto.TaskRequest;
import com.tasks.com.yaganaste.com.dto.TaskResponse;
import com.tasks.com.yaganaste.com.enums.TaskStatus;
import com.tasks.com.yaganaste.com.service.TaskService;
import com.tasks.com.yaganaste.com.dto.ErrorResponse;
import com.tasks.com.yaganaste.com.dto.ApiResponse;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tareas", description = "API para la gestión de tareas")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    // Inyección de dependencias por constructor
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Crea una nueva tarea.
     * POST /api/tasks
     */
    @PostMapping
    @Operation(
        summary = "Crear una nueva tarea",
        description = "Crea una nueva tarea con los datos proporcionados"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Tarea creada exitosamente",
            content = @Content(schema = @Schema(implementation = TaskResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody TaskRequest request) {
        
        logger.info("POST /api/tasks - Creando tarea: {}", request.getTitle());
        
        TaskResponse created = taskService.createTask(request);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tarea creada exitosamente", created));
    }

    /**
     * Obtiene todas las tareas.
     * GET /api/tasks
     */
    @GetMapping
    @Operation(
        summary = "Obtener todas las tareas",
        description = "Retorna una lista con todas las tareas registradas"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de tareas obtenida exitosamente"
        )
    })
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks() {
        
        logger.info("GET /api/tasks - Obteniendo todas las tareas");
        
        List<TaskResponse> tasks = taskService.getAllTasks();
        
        return ResponseEntity.ok(ApiResponse.success("Tareas obtenidas exitosamente", tasks));
    }

    /**
     * Obtiene una tarea por su ID.
     * GET /api/tasks/{id}
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener tarea por ID",
        description = "Retorna una tarea específica basada en su ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tarea encontrada",
            content = @Content(schema = @Schema(implementation = TaskResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Tarea no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @Parameter(description = "ID de la tarea", required = true)
            @PathVariable Long id) {
        
        logger.info("GET /api/tasks/{} - Obteniendo tarea", id);
        
        TaskResponse task = taskService.getTaskById(id);
        
        return ResponseEntity.ok(ApiResponse.success("Tarea obtenida exitosamente", task));
    }

    /**
     * Actualiza una tarea existente.
     * PUT /api/tasks/{id}
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar tarea",
        description = "Actualiza una tarea existente con los nuevos datos proporcionados"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tarea actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = TaskResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Tarea no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @Parameter(description = "ID de la tarea", required = true)
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        
        logger.info("PUT /api/tasks/{} - Actualizando tarea", id);
        
        TaskResponse updated = taskService.updateTask(id, request);
        
        return ResponseEntity.ok(ApiResponse.success("Tarea actualizada exitosamente", updated));
    }

    /**
     * Elimina una tarea.
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar tarea",
        description = "Elimina una tarea del sistema basada en su ID"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tarea eliminada exitosamente"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Tarea no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteTask(
            @Parameter(description = "ID de la tarea", required = true)
            @PathVariable Long id) {
        
        logger.info("DELETE /api/tasks/{} - Eliminando tarea", id);
        
        taskService.deleteTask(id);
        
        return ResponseEntity.ok(ApiResponse.success("Tarea eliminada exitosamente"));
    }

    /**
     * Obtiene tareas por estado.
     * GET /api/tasks/status/{status}
     */
    @GetMapping("/status/{status}")
    @Operation(
        summary = "Obtener tareas por estado",
        description = "Retorna todas las tareas que tienen el estado especificado"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tareas obtenidas exitosamente"
        )
    })
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getTasksByStatus(
            @Parameter(description = "Estado de las tareas (PENDING, IN_PROGRESS, COMPLETED)", required = true)
            @PathVariable TaskStatus status) {
        
        logger.info("GET /api/tasks/status/{} - Obteniendo tareas por estado", status);
        
        List<TaskResponse> tasks = taskService.getTasksByStatus(status);
        
        return ResponseEntity.ok(ApiResponse.success("Tareas obtenidas exitosamente", tasks));
    }

    /**
     * Busca tareas por término.
     * GET /api/tasks/search?q={searchTerm}
     */
    @GetMapping("/search")
    @Operation(
        summary = "Buscar tareas",
        description = "Busca tareas que contengan el término en título o descripción"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Búsqueda realizada exitosamente"
        )
    })
    public ResponseEntity<ApiResponse<List<TaskResponse>>> searchTasks(
            @Parameter(description = "Término de búsqueda", required = true)
            @RequestParam("q") String searchTerm) {
        
        logger.info("GET /api/tasks/search?q={} - Buscando tareas", searchTerm);
        
        List<TaskResponse> tasks = taskService.searchTasks(searchTerm);
        
        return ResponseEntity.ok(ApiResponse.success("Búsqueda completada", tasks));
    }

    /**
     * Obtiene estadísticas de tareas.
     * GET /api/tasks/stats
     */
    @GetMapping("/stats")
    @Operation(
        summary = "Obtener estadísticas",
        description = "Retorna el conteo de tareas por cada estado"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Estadísticas obtenidas exitosamente"
        )
    })
    public ResponseEntity<ApiResponse<TaskStats>> getTaskStats() {
        
        logger.info("GET /api/tasks/stats - Obteniendo estadísticas");
        
        TaskStats stats = new TaskStats(
                taskService.countTasksByStatus(TaskStatus.PENDING),
                taskService.countTasksByStatus(TaskStatus.IN_PROGRESS),
                taskService.countTasksByStatus(TaskStatus.COMPLETED)
        );
        
        return ResponseEntity.ok(ApiResponse.success("Estadísticas obtenidas", stats));
    }

    /**
     * Clase interna para las estadísticas.
     */
    @Schema(description = "Estadísticas de tareas")
    public record TaskStats(
            @Schema(description = "Tareas pendientes") long pending,
            @Schema(description = "Tareas en progreso") long inProgress,
            @Schema(description = "Tareas completadas") long completed
    ) {
        @Schema(description = "Total de tareas")
        public long total() {
            return pending + inProgress + completed;
        }
    }
}


