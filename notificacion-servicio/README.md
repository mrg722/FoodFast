# notificacion-servicio

Microservicio FoodFast encargado de registrar y simular el envío de notificaciones a clientes.

## Puerto
8089

## Base de datos
notificacion_db

## Reglas de negocio
- Una notificación nueva inicia en estado PENDIENTE.
- Solo se puede marcar como LEIDA una notificación enviada.
- No se puede editar una notificación ENVIADA o LEIDA.
- El envío es simulado: no se conecta a correo real todavía.

## Ejecutar
```bash
mvn clean spring-boot:run
```

## Crear base de datos
```sql
CREATE DATABASE IF NOT EXISTS notificacion_db;
```

## Endpoints principales
- GET http://localhost:8089/actuator/health
- GET http://localhost:8089/api/notificaciones
- POST http://localhost:8089/api/notificaciones
- GET http://localhost:8089/api/notificaciones/cliente/1
- PUT http://localhost:8089/api/notificaciones/1/enviar
- PUT http://localhost:8089/api/notificaciones/1/leer

## Seguridad futura
Cuando se active JWT, las peticiones protegidas deberán enviar:
`Authorization: Bearer <TOKEN>`.
El secreto debe tener mínimo 42 caracteres y ser igual al de autenticacion-servicio.
