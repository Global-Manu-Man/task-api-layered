package com.tasks.com.yaganaste.com.repository;

import com.tasks.com.yaganaste.com.enums.TaskStatus;
import com.tasks.com.yaganaste.com.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Busca tareas por estado.
     * @param status Estado de la tarea
     * @return Lista de tareas con el estado especificado
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Busca tareas que contengan el texto en el título (case insensitive).
     * @param title Texto a buscar en el título
     * @return Lista de tareas que coinciden
     */
    List<Task> findByTitleContainingIgnoreCase(String title);

    /**
     * Busca tareas creadas después de una fecha específica.
     * @param date Fecha de referencia
     * @return Lista de tareas creadas después de la fecha
     */
    List<Task> findByCreationDateAfter(LocalDateTime date);

    /**
     * Busca tareas por estado ordenadas por fecha de creación descendente.
     * @param status Estado de la tarea
     * @return Lista de tareas ordenadas
     */
    List<Task> findByStatusOrderByCreationDateDesc(TaskStatus status);

    /**
     * Cuenta el número de tareas por estado.
     * @param status Estado de la tarea
     * @return Número de tareas
     */
    long countByStatus(TaskStatus status);

    /**
     * Busca tareas que contengan texto en título o descripción.
     * @param searchTerm Término de búsqueda
     * @return Lista de tareas que coinciden
     */
    @Query("SELECT t FROM Task t WHERE " +
           "LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Task> searchByTitleOrDescription(@Param("searchTerm") String searchTerm);

    /**
     * Verifica si existe una tarea con el título exacto.
     * @param title Título a verificar
     * @return true si existe
     */
    boolean existsByTitleIgnoreCase(String title);
}

