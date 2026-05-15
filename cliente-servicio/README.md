# cliente-servicio - FoodFast

Microservicio encargado de administrar clientes y direcciones de entrega dentro del sistema FoodFast.

## Versiones principales

- Java 21
- Spring Boot 3.5.14
- Maven
- MySQL / Laragon
- Lombok
- Flyway
- Spring Data JPA
- Bean Validation
- Springdoc OpenAPI / Swagger
- Actuator

## Puerto y base de datos

```properties
server.port=8084
spring.datasource.url=jdbc:mysql://localhost:3306/cliente_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
```

## Responsabilidad

Este microservicio administra únicamente datos de clientes y direcciones. No comparte tablas con otros microservicios.

## Reglas de negocio

- No se permite duplicar email de cliente.
- Al crear o actualizar un cliente con direcciones, debe existir exactamente una dirección principal.
- Un cliente puede desactivarse sin eliminarse físicamente.
- Las direcciones pertenecen a un cliente y se eliminan en cascada cuando se elimina el cliente.

## Estructura CSR

```text
src/main/java/com/foodfast/cliente_servicio
├── controller
├── dto
├── exception
├── model
├── repository
└── service
```

## Flyway

Las tablas se crean con:

```text
src/main/resources/db/migration/V1__crear_tablas_clientes.sql
```

Como se usa Flyway, en `application.properties` se deja:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

## Ejecutar

1. Encender Laragon.
2. Verificar MySQL activo.
3. Crear la base si se desea manualmente:

```sql
CREATE DATABASE IF NOT EXISTS cliente_db;
```

4. Ejecutar:

```bash
mvn clean spring-boot:run
```

## Probar salud del servicio

```http
GET http://localhost:8084/actuator/health
```

## Swagger

```text
http://localhost:8084/swagger-ui/index.html
```

## Endpoints principales

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/clientes` | Lista clientes |
| GET | `/api/clientes/{id}` | Busca cliente por id |
| GET | `/api/clientes/email/{email}` | Busca cliente por email |
| POST | `/api/clientes` | Crea cliente |
| PUT | `/api/clientes/{id}` | Actualiza cliente |
| PUT | `/api/clientes/{id}/desactivar` | Desactiva cliente |
| DELETE | `/api/clientes/{id}` | Elimina cliente |

## Body ejemplo para crear cliente

```json
{
  "nombre": "Ana",
  "apellido": "Perez",
  "email": "ana.perez@demo.com",
  "telefono": "+56911112222",
  "direcciones": [
    {
      "calle": "Av. Providencia",
      "numero": "1234",
      "comuna": "Providencia",
      "ciudad": "Santiago",
      "referencia": "Departamento 501",
      "principal": true
    }
  ]
}
```

## Seguridad JWT futura

La seguridad no está activada en esta versión para no bloquear las pruebas CRUD. Cuando se implemente `autenticacion-servicio`, se usará un secreto JWT de mínimo 42 caracteres y el mismo secreto deberá estar en los servicios protegidos.

Ejemplo:

```properties
jwt.secret=FoodFastClaveJWTUltraSegura2026ConMasDe42CaracteresParaFirmarTokens
```

Las peticiones protegidas deberán usar:

```http
Authorization: Bearer <TOKEN>
```

## Commits sugeridos

```bash
git add .
git commit -m "feat(cliente-servicio): crear estructura Spring Boot 3.5.14"
git commit -m "feat(cliente-servicio): agregar entidades Cliente y Direccion"
git commit -m "feat(cliente-servicio): agregar Flyway y migracion inicial"
git commit -m "feat(cliente-servicio): implementar service con reglas de negocio"
git commit -m "feat(cliente-servicio): agregar controller REST y ApiResponse"
git commit -m "feat(cliente-servicio): agregar logs y manejo global de excepciones"
```
