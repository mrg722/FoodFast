# autenticacion-servicio - FoodFast

Microservicio encargado de registrar usuarios, iniciar sesión, cifrar contraseñas con BCrypt y generar tokens JWT.

## Puerto

```properties
server.port=8090
```

## Base de datos

```text
autenticacion_db
```

## Tecnologías

- Java 21
- Spring Boot 3.5.14
- Spring Web
- Spring Data JPA
- Spring Security
- BCrypt
- JWT
- MySQL
- Flyway
- Lombok
- Swagger / OpenAPI
- Actuator

## Consideración del profesor

El secreto JWT debe tener mínimo 42 caracteres y debe repetirse en los microservicios que validen el token.

```properties
jwt.secret=FoodFastClaveJWTUltraSegura2026ParaMicroserviciosConMasDe42Caracteres
```

## Crear base de datos

En Laragon / HeidiSQL:

```sql
CREATE DATABASE IF NOT EXISTS autenticacion_db;
```

## Ejecutar

```bash
mvn clean spring-boot:run
```

## Health

```http
GET http://localhost:8090/actuator/health
```

## Registrar usuario ADMIN

```http
POST http://localhost:8090/api/auth/registro
Content-Type: application/json
```

```json
{
  "nombre": "Administrador FoodFast",
  "email": "admin@foodfast.cl",
  "password": "123456",
  "rol": "ADMIN"
}
```

## Login

```http
POST http://localhost:8090/api/auth/login
Content-Type: application/json
```

```json
{
  "email": "admin@foodfast.cl",
  "password": "123456"
}
```

## Usar token en Postman

En la respuesta de login copia el token. Luego en endpoints protegidos agrega Header:

```text
Authorization: Bearer <TOKEN>
```

## Perfil autenticado

```http
GET http://localhost:8090/api/auth/perfil
Authorization: Bearer <TOKEN>
```

## Listar usuarios como ADMIN

```http
GET http://localhost:8090/api/auth/usuarios
Authorization: Bearer <TOKEN>
```

## Swagger

```text
http://localhost:8090/swagger-ui.html
```

## Importante

Este proyecto no incluye `CorsConfig.java` para conexión con frontend. Está preparado para pruebas en Laragon, Postman y Swagger.
