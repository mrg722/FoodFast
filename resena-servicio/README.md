# resena-servicio

Microservicio FoodFast encargado de registrar reseñas, comentarios y calificaciones de clientes sobre productos.

## Puerto
8088

## Base de datos
resena_db

## Reglas de negocio
- La calificación debe estar entre 1 y 5.
- Un cliente solo puede registrar una reseña por producto.
- Una reseña puede desactivarse sin eliminarla físicamente.
- Se puede consultar promedio de calificación por producto.

## Ejecutar
```bash
mvn clean spring-boot:run
```

## Crear base de datos
```sql
CREATE DATABASE IF NOT EXISTS resena_db;
```

## Endpoints principales
- GET http://localhost:8088/actuator/health
- GET http://localhost:8088/api/resenas
- POST http://localhost:8088/api/resenas
- GET http://localhost:8088/api/resenas/producto/1
- GET http://localhost:8088/api/resenas/producto/1/promedio
- PUT http://localhost:8088/api/resenas/1/desactivar

## Seguridad futura
Cuando se active JWT, las peticiones protegidas deberán enviar:
`Authorization: Bearer <TOKEN>`.
El secreto debe tener mínimo 42 caracteres y ser igual al de autenticacion-servicio.
