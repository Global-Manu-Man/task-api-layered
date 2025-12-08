-- =====================================================
-- V3__fix_constraints.sql
-- TASK API - Corrección de constraints
-- Usar si Hibernate reporta errores de DDL
-- =====================================================

-- ========== ELIMINAR DEFAULT CONSTRAINT DE STATUS ==========
DECLARE @constraintName NVARCHAR(200);

SELECT @constraintName = dc.name
FROM sys.default_constraints dc
INNER JOIN sys.columns c ON dc.parent_object_id = c.object_id AND dc.parent_column_id = c.column_id
WHERE dc.parent_object_id = OBJECT_ID('tasks') AND c.name = 'status';

IF @constraintName IS NOT NULL
BEGIN
    EXEC('ALTER TABLE tasks DROP CONSTRAINT ' + @constraintName);
    PRINT 'Default constraint eliminado: ' + @constraintName;
END
ELSE
BEGIN
    PRINT 'No se encontró default constraint en columna status';
END
GO

-- ========== ELIMINAR CHECK CONSTRAINT DE STATUS ==========
DECLARE @checkConstraintName NVARCHAR(200);

SELECT @checkConstraintName = cc.name
FROM sys.check_constraints cc
WHERE cc.parent_object_id = OBJECT_ID('tasks');

IF @checkConstraintName IS NOT NULL
BEGIN
    EXEC('ALTER TABLE tasks DROP CONSTRAINT ' + @checkConstraintName);
    PRINT 'Check constraint eliminado: ' + @checkConstraintName;
END
ELSE
BEGIN
    PRINT 'No se encontró check constraint en tabla tasks';
END
GO

-- ========== LISTAR CONSTRAINTS RESTANTES ==========
SELECT 
    'Default' AS tipo,
    dc.name AS constraint_name,
    c.name AS column_name
FROM sys.default_constraints dc
INNER JOIN sys.columns c ON dc.parent_object_id = c.object_id AND dc.parent_column_id = c.column_id
WHERE dc.parent_object_id = OBJECT_ID('tasks')
UNION ALL
SELECT 
    'Check' AS tipo,
    cc.name AS constraint_name,
    NULL AS column_name
FROM sys.check_constraints cc
WHERE cc.parent_object_id = OBJECT_ID('tasks');
GO
