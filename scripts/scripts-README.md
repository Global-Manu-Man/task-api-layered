# 📂 Scripts SQL

Este directorio contiene los scripts de base de datos para la API de tareas.

## 📁 Estructura

```
scripts/sql/
├── V1__create_tasks_table.sql   # Creación de tabla e índices
├── V2__insert_sample_data.sql   # Datos de prueba
├── V3__fix_constraints.sql      # Corrección de constraints (si es necesario)
└── README.md                    # Este archivo
```

## 🚀 Orden de Ejecución

Ejecutar los scripts en orden numérico:

| Orden | Script | Descripción |
|-------|--------|-------------|
| 1️⃣ | `V1__create_tasks_table.sql` | Crea la tabla `tasks` con índices |
| 2️⃣ | `V2__insert_sample_data.sql` | Inserta 8 tareas de prueba |
| 3️⃣ | `V3__fix_constraints.sql` | Solo si hay errores de constraints |

## 💻 Cómo Ejecutar

### Opción 1: Azure Portal (Query Editor)

1. Ve a **Azure Portal** → **SQL Database** → **taskdb**
2. Click en **Query editor (preview)**
3. Inicia sesión con tus credenciales
4. Pega el contenido del script y ejecuta

### Opción 2: Azure Data Studio

1. Conecta a tu servidor SQL Azure
2. Abre cada archivo `.sql`
3. Ejecuta con `F5` o el botón "Run"

### Opción 3: sqlcmd (Línea de comandos)

```bash
sqlcmd -S task-api-sql-server.database.windows.net -d taskdb -U sqladmin -P "tu_password" -i V1__create_tasks_table.sql
```

## ⚠️ Notas Importantes

- Los scripts son **idempotentes**: verifican si los objetos existen antes de crearlos
- `V3__fix_constraints.sql` solo es necesario si Hibernate reporta errores de DDL
- En producción, `spring.jpa.hibernate.ddl-auto=none` - los cambios de schema se hacen manualmente

## 🔄 Nomenclatura

Los scripts siguen la convención de **Flyway** para futuras migraciones:

```
V{version}__{descripcion}.sql
```

Ejemplos:
- `V1__create_tasks_table.sql`
- `V2__insert_sample_data.sql`
- `V4__add_priority_column.sql` (futuro)
