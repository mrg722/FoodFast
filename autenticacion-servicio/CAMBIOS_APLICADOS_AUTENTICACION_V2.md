# Cambios aplicados - autenticacion-servicio V2

Se entregó el microservicio completo para pruebas en Postman y Laragon, sin configuración CORS para frontend.

## Incluye

- Spring Boot 3.5.14
- Java 21
- MySQL
- Flyway
- Lombok
- Spring Security
- BCrypt
- JWT
- Swagger
- Actuator
- DTOs
- ApiResponse
- ErrorResponse
- Manejo global de excepciones
- Logs
- Validaciones Bean Validation

## Endpoints principales

- `POST /api/auth/registro`
- `POST /api/auth/login`
- `GET /api/auth/perfil`
- `GET /api/auth/usuarios`
- `GET /api/auth/usuarios/{id}`
- `PUT /api/auth/usuarios/{id}/activar`
- `PUT /api/auth/usuarios/{id}/desactivar`

## Seguridad

Endpoints públicos:

- `/api/auth/registro`
- `/api/auth/login`
- `/actuator/health`
- Swagger

Endpoints protegidos:

- `/api/auth/perfil`
- `/api/auth/usuarios`
- `/api/auth/usuarios/{id}`
- activar/desactivar usuarios

Probar endpoints protegidos usar:

```text
Authorization: Bearer <TOKEN>
```
