package com.tasks.com.yaganaste.com;
import com.tasks.com.yaganaste.com.controller.TaskController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tasks.com.yaganaste.com.dto.TaskRequest;
import com.tasks.com.yaganaste.com.dto.TaskResponse;
import com.tasks.com.yaganaste.com.entity.Task;
import com.tasks.com.yaganaste.com.enums.TaskStatus;
import com.tasks.com.yaganaste.com.exception.GlobalExceptionHandler;
import com.tasks.com.yaganaste.com.exception.ResourceNotFoundException;
import com.tasks.com.yaganaste.com.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para TaskController.
 * Compatible con Spring Boot 4.0
 * Utiliza MockMvc standalone setup con Mockito.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TaskController Unit Tests")
class TaskControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    // Datos de prueba reutilizables
    private TaskRequest validTaskRequest;
    private TaskResponse taskResponse1;
    private TaskResponse taskResponse2;
    private TaskResponse taskResponse3;

    @BeforeEach
    void setUp() {
        // Configurar MockMvc con standalone setup (compatible con Spring Boot 4.0)
        mockMvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Configurar ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configurar request válido
        validTaskRequest = new TaskRequest();
        validTaskRequest.setTitle("Tarea de prueba");
        validTaskRequest.setDescription("Descripción de la tarea de prueba");
        validTaskRequest.setStatus(TaskStatus.PENDING);

        // Configurar responses de prueba
        taskResponse1 = createTaskResponse(1L, "Tarea 1", "Descripción 1", TaskStatus.PENDING);
        taskResponse2 = createTaskResponse(2L, "Tarea 2", "Descripción 2", TaskStatus.IN_PROGRESS);
        taskResponse3 = createTaskResponse(3L, "Tarea 3", "Descripción 3", TaskStatus.COMPLETED);
    }

    /**
     * Método helper para crear TaskResponse de prueba.
     * Usa el constructor de TaskResponse que recibe Task.
     */
    private TaskResponse createTaskResponse(Long id, String title, String description, TaskStatus status) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setCreationDate(LocalDateTime.now());
        task.setUpdatedDate(LocalDateTime.now());
        return new TaskResponse(task);
    }

    // ========================================
    // TESTS PARA POST /api/tasks
    // ========================================
    @Nested
    @DisplayName("POST /api/tasks - Crear Tarea")
    class CreateTaskTests {

        @Test
        @DisplayName("Debe crear una tarea exitosamente con datos válidos")
        void createTask_WithValidData_ShouldReturnCreated() throws Exception {
            // Given
            given(taskService.createTask(any(TaskRequest.class))).willReturn(taskResponse1);

            // When
            ResultActions result = mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validTaskRequest)));

            // Then
            result.andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.message", containsString("creada")))
                    .andExpect(jsonPath("$.data.id", is(1)))
                    .andExpect(jsonPath("$.data.title", is("Tarea 1")));

            verify(taskService, times(1)).createTask(any(TaskRequest.class));
        }

        @Test
        @DisplayName("Debe crear tarea sin descripción (campo opcional)")
        void createTask_WithoutDescription_ShouldReturnCreated() throws Exception {
            // Given
            TaskRequest requestWithoutDescription = new TaskRequest();
            requestWithoutDescription.setTitle("Tarea sin descripción");
            requestWithoutDescription.setStatus(TaskStatus.PENDING);

            given(taskService.createTask(any(TaskRequest.class))).willReturn(taskResponse1);

            // When
            ResultActions result = mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestWithoutDescription)));

            // Then
            result.andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success", is(true)));

            verify(taskService, times(1)).createTask(any(TaskRequest.class));
        }
    }

    // ========================================
    // TESTS PARA GET /api/tasks
    // ========================================
    @Nested
    @DisplayName("GET /api/tasks - Obtener Todas las Tareas")
    class GetAllTasksTests {

        @Test
        @DisplayName("Debe retornar lista de tareas exitosamente")
        void getAllTasks_ShouldReturnListOfTasks() throws Exception {
            // Given
            List<TaskResponse> tasks = Arrays.asList(taskResponse1, taskResponse2, taskResponse3);
            given(taskService.getAllTasks()).willReturn(tasks);

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data", hasSize(3)))
                    .andExpect(jsonPath("$.data[0].id", is(1)))
                    .andExpect(jsonPath("$.data[1].id", is(2)))
                    .andExpect(jsonPath("$.data[2].id", is(3)));

            verify(taskService, times(1)).getAllTasks();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay tareas")
        void getAllTasks_WhenNoTasks_ShouldReturnEmptyList() throws Exception {
            // Given
            given(taskService.getAllTasks()).willReturn(Collections.emptyList());

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data", hasSize(0)));

            verify(taskService, times(1)).getAllTasks();
        }
    }

    // ========================================
    // TESTS PARA GET /api/tasks/{id}
    // ========================================
    @Nested
    @DisplayName("GET /api/tasks/{id} - Obtener Tarea por ID")
    class GetTaskByIdTests {

        @Test
        @DisplayName("Debe retornar tarea cuando existe")
        void getTaskById_WhenTaskExists_ShouldReturnTask() throws Exception {
            // Given
            given(taskService.getTaskById(1L)).willReturn(taskResponse1);

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data.id", is(1)))
                    .andExpect(jsonPath("$.data.title", is("Tarea 1")));

            verify(taskService, times(1)).getTaskById(1L);
        }

        @Test
        @DisplayName("Debe retornar 404 cuando la tarea no existe")
        void getTaskById_WhenTaskNotExists_ShouldReturnNotFound() throws Exception {
            // Given
            given(taskService.getTaskById(999L))
                    .willThrow(new ResourceNotFoundException("Tarea", "id", 999L));

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/999")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isNotFound());

            verify(taskService, times(1)).getTaskById(999L);
        }
    }

    // ========================================
    // TESTS PARA PUT /api/tasks/{id}
    // ========================================
    @Nested
    @DisplayName("PUT /api/tasks/{id} - Actualizar Tarea")
    class UpdateTaskTests {

        @Test
        @DisplayName("Debe actualizar tarea exitosamente")
        void updateTask_WithValidData_ShouldReturnUpdatedTask() throws Exception {
            // Given
            TaskRequest updateRequest = new TaskRequest();
            updateRequest.setTitle("Tarea Actualizada");
            updateRequest.setDescription("Nueva descripción");
            updateRequest.setStatus(TaskStatus.IN_PROGRESS);

            TaskResponse updatedResponse = createTaskResponse(1L, "Tarea Actualizada",
                    "Nueva descripción", TaskStatus.IN_PROGRESS);

            given(taskService.updateTask(eq(1L), any(TaskRequest.class))).willReturn(updatedResponse);

            // When
            ResultActions result = mockMvc.perform(put("/api/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.message", containsString("actualizada")))
                    .andExpect(jsonPath("$.data.title", is("Tarea Actualizada")));

            verify(taskService, times(1)).updateTask(eq(1L), any(TaskRequest.class));
        }

        @Test
        @DisplayName("Debe retornar 404 cuando la tarea a actualizar no existe")
        void updateTask_WhenTaskNotExists_ShouldReturnNotFound() throws Exception {
            // Given
            given(taskService.updateTask(eq(999L), any(TaskRequest.class)))
                    .willThrow(new ResourceNotFoundException("Tarea", "id", 999L));

            // When
            ResultActions result = mockMvc.perform(put("/api/tasks/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(validTaskRequest)));

            // Then
            result.andDo(print())
                    .andExpect(status().isNotFound());

            verify(taskService, times(1)).updateTask(eq(999L), any(TaskRequest.class));
        }
    }

    // ========================================
    // TESTS PARA DELETE /api/tasks/{id}
    // ========================================
    @Nested
    @DisplayName("DELETE /api/tasks/{id} - Eliminar Tarea")
    class DeleteTaskTests {

        @Test
        @DisplayName("Debe eliminar tarea exitosamente")
        void deleteTask_WhenTaskExists_ShouldReturnSuccess() throws Exception {
            // Given
            doNothing().when(taskService).deleteTask(1L);

            // When
            ResultActions result = mockMvc.perform(delete("/api/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.message", containsString("eliminada")));

            verify(taskService, times(1)).deleteTask(1L);
        }

        @Test
        @DisplayName("Debe retornar 404 cuando la tarea a eliminar no existe")
        void deleteTask_WhenTaskNotExists_ShouldReturnNotFound() throws Exception {
            // Given
            doThrow(new ResourceNotFoundException("Tarea", "id", 999L))
                    .when(taskService).deleteTask(999L);

            // When
            ResultActions result = mockMvc.perform(delete("/api/tasks/999")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isNotFound());

            verify(taskService, times(1)).deleteTask(999L);
        }
    }

    // ========================================
    // TESTS PARA GET /api/tasks/status/{status}
    // ========================================
    @Nested
    @DisplayName("GET /api/tasks/status/{status} - Filtrar por Estado")
    class GetTasksByStatusTests {

        @Test
        @DisplayName("Debe retornar tareas con estado PENDING")
        void getTasksByStatus_Pending_ShouldReturnPendingTasks() throws Exception {
            // Given
            List<TaskResponse> pendingTasks = Arrays.asList(taskResponse1);
            given(taskService.getTasksByStatus(TaskStatus.PENDING)).willReturn(pendingTasks);

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/status/PENDING")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andExpect(jsonPath("$.data[0].id", is(1)));

            verify(taskService, times(1)).getTasksByStatus(TaskStatus.PENDING);
        }

        @Test
        @DisplayName("Debe retornar tareas con estado IN_PROGRESS")
        void getTasksByStatus_InProgress_ShouldReturnInProgressTasks() throws Exception {
            // Given
            List<TaskResponse> inProgressTasks = Arrays.asList(taskResponse2);
            given(taskService.getTasksByStatus(TaskStatus.IN_PROGRESS)).willReturn(inProgressTasks);

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/status/IN_PROGRESS")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andExpect(jsonPath("$.data[0].id", is(2)));

            verify(taskService, times(1)).getTasksByStatus(TaskStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("Debe retornar tareas con estado COMPLETED")
        void getTasksByStatus_Completed_ShouldReturnCompletedTasks() throws Exception {
            // Given
            List<TaskResponse> completedTasks = Arrays.asList(taskResponse3);
            given(taskService.getTasksByStatus(TaskStatus.COMPLETED)).willReturn(completedTasks);

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/status/COMPLETED")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andExpect(jsonPath("$.data[0].id", is(3)));

            verify(taskService, times(1)).getTasksByStatus(TaskStatus.COMPLETED);
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay tareas con el estado")
        void getTasksByStatus_WhenNoTasks_ShouldReturnEmptyList() throws Exception {
            // Given
            given(taskService.getTasksByStatus(TaskStatus.COMPLETED)).willReturn(Collections.emptyList());

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/status/COMPLETED")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data", hasSize(0)));

            verify(taskService, times(1)).getTasksByStatus(TaskStatus.COMPLETED);
        }
    }

    // ========================================
    // TESTS PARA GET /api/tasks/search
    // ========================================
    @Nested
    @DisplayName("GET /api/tasks/search - Buscar Tareas")
    class SearchTasksTests {

        @Test
        @DisplayName("Debe retornar tareas que coincidan con el término de búsqueda")
        void searchTasks_WithMatchingTerm_ShouldReturnMatchingTasks() throws Exception {
            // Given
            List<TaskResponse> matchingTasks = Arrays.asList(taskResponse1, taskResponse2);
            given(taskService.searchTasks("Tarea")).willReturn(matchingTasks);

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/search")
                    .param("q", "Tarea")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data", hasSize(2)));

            verify(taskService, times(1)).searchTasks("Tarea");
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay coincidencias")
        void searchTasks_WithNoMatches_ShouldReturnEmptyList() throws Exception {
            // Given
            given(taskService.searchTasks("xyz123")).willReturn(Collections.emptyList());

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/search")
                    .param("q", "xyz123")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data", hasSize(0)));

            verify(taskService, times(1)).searchTasks("xyz123");
        }
    }

    // ========================================
    // TESTS PARA GET /api/tasks/stats
    // ========================================
    @Nested
    @DisplayName("GET /api/tasks/stats - Obtener Estadísticas")
    class GetTaskStatsTests {

        @Test
        @DisplayName("Debe retornar estadísticas de tareas")
        void getTaskStats_ShouldReturnStatistics() throws Exception {
            // Given
            given(taskService.countTasksByStatus(TaskStatus.PENDING)).willReturn(5L);
            given(taskService.countTasksByStatus(TaskStatus.IN_PROGRESS)).willReturn(3L);
            given(taskService.countTasksByStatus(TaskStatus.COMPLETED)).willReturn(10L);

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/stats")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data.pending", is(5)))
                    .andExpect(jsonPath("$.data.inProgress", is(3)))
                    .andExpect(jsonPath("$.data.completed", is(10)));

            verify(taskService, times(1)).countTasksByStatus(TaskStatus.PENDING);
            verify(taskService, times(1)).countTasksByStatus(TaskStatus.IN_PROGRESS);
            verify(taskService, times(1)).countTasksByStatus(TaskStatus.COMPLETED);
        }

        @Test
        @DisplayName("Debe retornar ceros cuando no hay tareas")
        void getTaskStats_WhenNoTasks_ShouldReturnZeros() throws Exception {
            // Given
            given(taskService.countTasksByStatus(TaskStatus.PENDING)).willReturn(0L);
            given(taskService.countTasksByStatus(TaskStatus.IN_PROGRESS)).willReturn(0L);
            given(taskService.countTasksByStatus(TaskStatus.COMPLETED)).willReturn(0L);

            // When
            ResultActions result = mockMvc.perform(get("/api/tasks/stats")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data.pending", is(0)))
                    .andExpect(jsonPath("$.data.inProgress", is(0)))
                    .andExpect(jsonPath("$.data.completed", is(0)));
        }
    }

    // ========================================
    // TESTS DE CASOS EDGE
    // ========================================
    @Nested
    @DisplayName("Edge Cases - Casos Límite")
    class EdgeCaseTests {

        @Test
        @DisplayName("Debe manejar JSON malformado")
        void request_WithMalformedJson_ShouldReturnBadRequest() throws Exception {
            // When
            ResultActions result = mockMvc.perform(post("/api/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{invalid json}"));

            // Then
            result.andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}