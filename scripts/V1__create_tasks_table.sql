-- =====================================================
-- V1__create_tasks_table.sql
-- TASK API - Creación de tabla principal
-- Base de datos: Azure SQL Server
-- =====================================================

-- ========== CREAR TABLA TASKS ==========
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'tasks')
BEGIN
    CREATE TABLE tasks (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        title NVARCHAR(255) NOT NULL,
        description NVARCHAR(MAX) NULL,
        status NVARCHAR(50) NOT NULL DEFAULT 'PENDING',
        creation_date DATETIME2 NOT NULL DEFAULT GETDATE(),
        updated_date DATETIME2 NOT NULL DEFAULT GETDATE()
    );
    
    PRINT 'Tabla tasks creada exitosamente';
END
ELSE
BEGIN
    PRINT 'La tabla tasks ya existe';
END
GO

-- ========== CREAR ÍNDICES ==========
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_tasks_status')
BEGIN
    CREATE INDEX IX_tasks_status ON tasks(status);
    PRINT 'Índice IX_tasks_status creado';
END
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_tasks_creation_date')
BEGIN
    CREATE INDEX IX_tasks_creation_date ON tasks(creation_date DESC);
    PRINT 'Índice IX_tasks_creation_date creado';
END
GO

-- ========== VERIFICAR ESTRUCTURA ==========
SELECT 
    COLUMN_NAME AS 'Columna',
    DATA_TYPE AS 'Tipo',
    CHARACTER_MAXIMUM_LENGTH AS 'Longitud',
    IS_NULLABLE AS 'Nullable',
    COLUMN_DEFAULT AS 'Default'
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'tasks'
ORDER BY ORDINAL_POSITION;
GO
