# FoodFast - Backend con Microservicios

## 1. Descripción general

FoodFast es un proyecto backend de e-commerce de comida online, similar a PedidosYa, Uber Eats o Rappi.

El sistema organiza procesos de una plataforma de comida: catálogo de productos, control de inventario, pedidos, clientes, restaurantes, pagos, reparto, reseñas, notificaciones y autenticación.

El proyecto usa arquitectura de microservicios con Java 21, Spring Boot 3.5.14, Spring Data JPA, MySQL, Flyway, DTOs, validaciones, manejo de excepciones, logs, Swagger, Actuator, Postman, Git y GitHub.

## 2. Objetivo del proyecto

Desarrollar un sistema backend dividido en microservicios independientes, donde cada microservicio tenga una responsabilidad específica, una base de datos propia y endpoints REST.

Objetivos técnicos:

- Aplicar patrón CSR: Controller, Service, Repository y Model.
- Usar DTO Request y DTO Response.
- Implementar CRUD y reglas de negocio.
- Usar MySQL y Flyway para persistencia.
- Validar datos con Bean Validation.
- Manejar errores con respuestas JSON uniformes.
- Registrar logs para trazabilidad.
- Implementar seguridad con BCrypt, JWT y roles.
- Probar endpoints con Postman y Swagger.
- Evidenciar trabajo mediante GitHub y tareas del equipo.

## 3. Integrantes

- Martin Reyes G.
- Damian Galaz

## 4. Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje principal |
| Spring Boot 3.5.14 | Framework backend |
| Spring Web | APIs REST |
| Spring Data JPA | Persistencia |
| MySQL | Base de datos |
| Laragon | Entorno local MySQL |
| Flyway | Migraciones de base de datos |
| Lombok | Reducir código repetitivo |
| Bean Validation | Validaciones |
| Spring Security | Seguridad |
| BCrypt | Cifrado de contraseñas |
| JWT | Autenticación por token |
| Swagger / Springdoc | Documentación de API |
| Actuator | Estado del microservicio |
| Postman | Pruebas REST |
| Git / GitHub | Control de versiones |
| Trello / tareas | Organización del equipo |

## 5. Arquitectura general

FoodFast está compuesto por microservicios independientes.

Flujo general:

```text
Postman / Frontend / Cliente externo
        ↓
API REST
        ↓
Microservicio Spring Boot
        ↓
Base de datos propia
```

Regla importante:

```text
Un microservicio no entra directamente a la base de datos de otro microservicio.
Si necesita información remota, debe consumir la API REST del otro servicio.
```

## 6. Microservicios del sistema

| N° | Microservicio | Puerto | Base de datos | Función |
|---:|---|---:|---|---|
| 1 | catalogo-servicio | 8081 | catalogo_db | Administra productos y categorías |
| 2 | inventario-servicio | 8082 | inventario_db | Controla stock disponible |
| 3 | pedido-servicio | 8083 | pedido_db | Registra pedidos y consulta inventario |
| 4 | cliente-servicio | 8084 | cliente_db | Administra clientes y direcciones |
| 5 | restaurante-servicio | 8085 | restaurante_db | Administra restaurantes y estado abierto/cerrado |
| 6 | pago-servicio | 8086 | pago_db | Registra pagos simulados |
| 7 | reparto-servicio | 8087 | reparto_db | Gestiona repartidores y entregas |
| 8 | resena-servicio | 8088 | resena_db | Administra reseñas y calificaciones |
| 9 | notificacion-servicio | 8089 | notificacion_db | Registra notificaciones del cliente |
| 10 | autenticacion-servicio | 8090 | autenticacion_db | Maneja usuarios, login, roles, BCrypt y JWT |

## 7. Patrón CSR aplicado

Cada microservicio sigue una estructura por capas:

| Capa | Responsabilidad |
|---|---|
| Controller | Recibe peticiones HTTP y devuelve respuestas REST |
| Service | Contiene reglas de negocio |
| Repository | Accede a la base de datos con JpaRepository |
| Model | Representa entidades JPA y tablas |
| DTO | Controla datos de entrada y salida |
| Exception | Maneja errores de forma uniforme |
| Resources | Contiene application.properties y migraciones Flyway |

Flujo interno:

```text
Postman
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
MySQL
```

## 8. Bases de datos

Cada microservicio usa su propia base de datos:

```sql
CREATE DATABASE IF NOT EXISTS catalogo_db;
CREATE DATABASE IF NOT EXISTS inventario_db;
CREATE DATABASE IF NOT EXISTS pedido_db;
CREATE DATABASE IF NOT EXISTS cliente_db;
CREATE DATABASE IF NOT EXISTS restaurante_db;
CREATE DATABASE IF NOT EXISTS pago_db;
CREATE DATABASE IF NOT EXISTS reparto_db;
CREATE DATABASE IF NOT EXISTS resena_db;
CREATE DATABASE IF NOT EXISTS notificacion_db;
CREATE DATABASE IF NOT EXISTS autenticacion_db;
```

Las tablas se crean y versionan con Flyway desde:

```text
src/main/resources/db/migration
```

Ejemplos:

```text
V1__crear_tabla_inventarios.sql
V1__crear_tabla_usuarios.sql
```

## 9. Microservicios principales para la defensa

### inventario-servicio

Objetivo: controlar el stock disponible de cada producto.

Reglas principales:

- No permitir stock negativo.
- Consultar stock por productoId.
- Descontar stock.
- No descontar más cantidad que la disponible.

Endpoints principales:

```http
POST http://localhost:8082/api/inventarios
GET  http://localhost:8082/api/inventarios
GET  http://localhost:8082/api/inventarios/stock/{productoId}
POST http://localhost:8082/api/inventarios/descontar
```

### autenticacion-servicio

Objetivo: registrar usuarios, cifrar contraseñas y generar tokens JWT.

Reglas principales:

- No guardar contraseñas en texto plano.
- Usar BCrypt para cifrar contraseñas.
- Generar JWT al iniciar sesión.
- Proteger endpoints con Authorization Bearer Token.

Endpoints principales:

```http
POST http://localhost:8090/api/auth/registro
POST http://localhost:8090/api/auth/login
GET  http://localhost:8090/api/auth/perfil
```

Header para endpoints protegidos:

```text
Authorization: Bearer <TOKEN>
```

### pedido-servicio / reparto-servicio

pedido-servicio puede comunicarse con inventario-servicio para validar stock antes de confirmar un pedido.

reparto-servicio permite demostrar flujo de estados y disponibilidad de repartidor.

## 10. Seguridad con JWT

El microservicio autenticacion-servicio genera un token JWT cuando el usuario inicia sesión correctamente.

Flujo:

```text
Usuario hace login
        ↓
autenticacion-servicio valida email y password
        ↓
se genera JWT
        ↓
cliente usa Authorization: Bearer <TOKEN>
        ↓
endpoints protegidos validan el token
```

El secreto JWT debe tener al menos 42 caracteres y debe ser el mismo en los microservicios que generen o validen el token.

Ejemplo:

```properties
jwt.secret=foodfast_secret_key_segura_para_jwt_minimo_42_caracteres_2026
jwt.expiration=86400000
```

## 11. Códigos HTTP esperados

| Código | Significado | Ejemplo |
|---:|---|---|
| 200 | OK | Listar inventarios o login correcto |
| 201 | Created | Crear inventario o registrar usuario |
| 204 | No Content | Eliminar recurso sin devolver body |
| 400 | Bad Request | Stock insuficiente o datos inválidos |
| 401 | Unauthorized | Acceder sin token |
| 403 | Forbidden | Token válido pero sin permiso |
| 404 | Not Found | Recurso inexistente |
| 409 | Conflict | Email duplicado |
| 500 | Internal Server Error | Error inesperado no controlado |

## 12. Pruebas principales con Postman

### Inventario

Crear inventario:

```http
POST http://localhost:8082/api/inventarios
```

Body:

```json
{
  "productoId": 1,
  "cantidadDisponible": 50,
  "cantidadReservada": 0,
  "ubicacion": "Bodega principal"
}
```

Consultar stock:

```http
GET http://localhost:8082/api/inventarios/stock/1
```

Descontar stock:

```http
POST http://localhost:8082/api/inventarios/descontar
```

Body:

```json
{
  "productoId": 1,
  "cantidad": 5
}
```

Forzar error 400:

```json
{
  "productoId": 1,
  "cantidad": 999
}
```

### Autenticación

Registrar usuario:

```http
POST http://localhost:8090/api/auth/registro
```

Body:

```json
{
  "nombre": "Administrador FoodFast",
  "email": "admin@foodfast.cl",
  "password": "123456",
  "rol": "ADMIN"
}
```

Login:

```http
POST http://localhost:8090/api/auth/login
```

Body:

```json
{
  "email": "admin@foodfast.cl",
  "password": "123456"
}
```

Perfil protegido:

```http
GET http://localhost:8090/api/auth/perfil
```

Header:

```text
Authorization: Bearer <TOKEN>
```

## 13. Swagger y Actuator

Actuator permite revisar si el microservicio está activo:

```http
GET http://localhost:8082/actuator/health
GET http://localhost:8090/actuator/health
```

Respuesta esperada:

```json
{
  "status": "UP"
}
```

Swagger permite revisar endpoints desde navegador:

```text
http://localhost:8082/swagger-ui.html
http://localhost:8090/swagger-ui.html
```

## 14. Cómo ejecutar

1. Iniciar Laragon y MySQL.
2. Crear las bases de datos necesarias.
3. Abrir cada microservicio en VS Code.
4. Ejecutar:

```bash
mvn clean spring-boot:run
```

Ejemplo:

```bash
cd inventario-servicio-v2
mvn clean spring-boot:run
```

## 15. Evidencias para la entrega

La entrega grupal considera:

- README general.
- Enlace a GitHub.
- Evidencia por rúbrica.
- Colección Postman.
- Presentación PPT exportada a PDF.
- Evidencia de Trello o tareas.
- Capturas de bases de datos.
- Capturas de Postman.
- Capturas de estructura CSR.
- Capturas de logs.

## 16. GitHub y trabajo colaborativo

Ejemplos de commits técnicos:

```bash
feat(inventario): implementar descuento de stock
feat(auth): agregar login con JWT
fix(reparto): corregir disponibilidad de repartidor
docs(readme): documentar ejecución de microservicios
test(postman): agregar colección de pruebas
```

## 17. Organización del equipo

### Martin Reyes G.

- Arquitectura general.
- inventario-servicio.
- autenticacion-servicio.
- errores HTTP.
- logs.
- JWT.
- pruebas en Postman.

### Damian Galaz

- pedido-servicio o reparto-servicio.
- comunicación entre microservicios.
- bases de datos.
- GitHub/Trello.
- evidencias.
- pruebas REST.

## 18. Conclusión

FoodFast demuestra una arquitectura backend basada en microservicios desacoplados, con bases de datos independientes, APIs REST, reglas de negocio, validaciones, manejo de excepciones, logs, seguridad con JWT y evidencia en GitHub.

El proyecto puede probarse mediante Postman, Swagger, Laragon/MySQL y terminal, permitiendo demostrar el flujo completo desde la petición HTTP hasta la persistencia en base de datos.
