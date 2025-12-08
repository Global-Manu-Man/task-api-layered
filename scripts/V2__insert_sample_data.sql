-- =====================================================
-- V2__insert_sample_data.sql
-- TASK API - Datos de prueba
-- Base de datos: Azure SQL Server
-- =====================================================

-- ========== INSERTAR DATOS DE PRUEBA ==========
-- Solo inserta si la tabla está vacía
IF NOT EXISTS (SELECT 1 FROM tasks)
BEGIN
    INSERT INTO tasks (title, description, status, creation_date, updated_date) VALUES
    ('Revisar documentación del proyecto', 'Leer y analizar la documentación técnica existente', 'PENDING', GETDATE(), GETDATE()),
    ('Configurar entorno de desarrollo', 'Instalar JDK 17, Maven y configurar IDE', 'COMPLETED', GETDATE(), GETDATE()),
    ('Implementar autenticación JWT', 'Agregar seguridad con tokens JWT al API', 'IN_PROGRESS', GETDATE(), GETDATE()),
    ('Escribir pruebas unitarias', 'Crear tests para los servicios principales', 'PENDING', GETDATE(), GETDATE()),
    ('Desplegar en servidor de pruebas', 'Realizar deployment en ambiente de QA', 'PENDING', GETDATE(), GETDATE()),
    ('Configurar pipeline CI/CD', 'Implementar pipeline en Azure DevOps con stages de build, test y deploy', 'COMPLETED', GETDATE(), GETDATE()),
    ('Refactorizar capa de servicios', 'Aplicar patrón hexagonal y mejorar separación de responsabilidades', 'IN_PROGRESS', GETDATE(), GETDATE()),
    ('Migrar base de datos a SQL Server', 'Configurar Azure SQL Database y actualizar connection strings', 'COMPLETED', GETDATE(), GETDATE());
    
    PRINT '8 tareas de prueba insertadas correctamente';
END
ELSE
BEGIN
    PRINT 'La tabla tasks ya contiene datos - No se insertaron registros';
END
GO

-- ========== VERIFICAR DATOS INSERTADOS ==========
SELECT 
    id,
    title,
    status,
    creation_date
FROM tasks
ORDER BY id;
GO

-- ========== ESTADÍSTICAS ==========
SELECT 
    status AS 'Estado',
    COUNT(*) AS 'Cantidad'
FROM tasks
GROUP BY status
ORDER BY status;
GO
