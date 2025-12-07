package com.tasks.com.yaganaste.com;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tasks.com.yaganaste.com.dto.TaskRequest;
import com.tasks.com.yaganaste.com.entity.Task;
import com.tasks.com.yaganaste.com.enums.TaskStatus;
import com.tasks.com.yaganaste.com.repository.TaskRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración completos para TaskController.
 * Utiliza base de datos H2 en memoria para pruebas reales.
 * Compatible con Spring Boot 4.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("TaskController Integration Tests")
class TaskControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    // Crear ObjectMapper manualmente (no inyectado)
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        // Configuración manual de MockMvc (compatible con Spring Boot 4.0)
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        // Crear ObjectMapper manualmente
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

        // Limpiar la base de datos antes de cada test
        taskRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
    }

    // ========================================
    // TESTS DE FLUJO COMPLETO CRUD
    // ========================================

    @Test
    @Order(1)
    @DisplayName("Flujo completo: Crear, Leer, Actualizar y Eliminar tarea")
    void fullCrudFlow_ShouldWorkCorrectly() throws Exception {
        // 1. CREAR
        TaskRequest createRequest = new TaskRequest();
        createRequest.setTitle("Tarea de prueba CRUD");
        createRequest.setDescription("Descripción para test CRUD");
        createRequest.setStatus(TaskStatus.PENDING);

        String createResponse = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long taskId = objectMapper.readTree(createResponse).path("data").path("id").asLong();

        // 2. LEER
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Tarea de prueba CRUD")));

        // 3. ACTUALIZAR
        TaskRequest updateRequest = new TaskRequest();
        updateRequest.setTitle("Tarea Actualizada");
        updateRequest.setDescription("Nueva descripción");
        updateRequest.setStatus(TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/api/tasks/" + taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Tarea Actualizada")));

        // 4. ELIMINAR
        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isOk());

        // 5. VERIFICAR ELIMINACIÓN
        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }

    // ========================================
    // TESTS PARA POST /api/tasks
    // ========================================

    @Test
    @DisplayName("POST /api/tasks - Crear tarea exitosamente")
    void createTask_ShouldReturnCreated() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Tarea Nueva");
        request.setDescription("Descripción de la tarea");
        request.setStatus(TaskStatus.PENDING);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.title", is("Tarea Nueva")));
    }

    @Test
    @DisplayName("POST /api/tasks - Título vacío retorna 400")
    void createTask_WithEmptyTitle_ShouldReturnBadRequest() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("");

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ========================================
    // TESTS PARA GET /api/tasks
    // ========================================

    @Test
    @DisplayName("GET /api/tasks - Obtener todas las tareas")
    void getAllTasks_ShouldReturnList() throws Exception {
        createTestTask("Tarea 1", TaskStatus.PENDING);
        createTestTask("Tarea 2", TaskStatus.IN_PROGRESS);

        mockMvc.perform(get("/api/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    // ========================================
    // TESTS PARA GET /api/tasks/{id}
    // ========================================

    @Test
    @DisplayName("GET /api/tasks/{id} - Tarea existe")
    void getTaskById_ShouldReturnTask() throws Exception {
        Task task = createTestTask("Tarea Test", TaskStatus.PENDING);

        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Tarea Test")));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} - Tarea no existe retorna 404")
    void getTaskById_NotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/99999"))
                .andExpect(status().isNotFound());
    }

    // ========================================
    // TESTS PARA GET /api/tasks/status/{status}
    // ========================================

    @Test
    @DisplayName("GET /api/tasks/status/PENDING - Filtrar por estado")
    void getTasksByStatus_ShouldReturnFiltered() throws Exception {
        createTestTask("Pending 1", TaskStatus.PENDING);
        createTestTask("Pending 2", TaskStatus.PENDING);
        createTestTask("Completed", TaskStatus.COMPLETED);

        mockMvc.perform(get("/api/tasks/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    // ========================================
    // TESTS PARA GET /api/tasks/search
    // ========================================

    @Test
    @DisplayName("GET /api/tasks/search - Buscar tareas")
    void searchTasks_ShouldReturnMatching() throws Exception {
        createTestTask("Documentación API", TaskStatus.PENDING);
        createTestTask("Testing", TaskStatus.IN_PROGRESS);

        mockMvc.perform(get("/api/tasks/search").param("q", "API"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    // ========================================
    // TESTS PARA GET /api/tasks/stats
    // ========================================

    @Test
    @DisplayName("GET /api/tasks/stats - Estadísticas")
    void getTaskStats_ShouldReturnCounts() throws Exception {
        createTestTask("P1", TaskStatus.PENDING);
        createTestTask("P2", TaskStatus.PENDING);
        createTestTask("IP", TaskStatus.IN_PROGRESS);
        createTestTask("C", TaskStatus.COMPLETED);

        mockMvc.perform(get("/api/tasks/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.pending", is(2)))
                .andExpect(jsonPath("$.data.inProgress", is(1)))
                .andExpect(jsonPath("$.data.completed", is(1)));
    }

    // ========================================
    // HELPER
    // ========================================

    private Task createTestTask(String title, TaskStatus status) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription("Descripción de " + title);
        task.setStatus(status);
        return taskRepository.save(task);
    }
}