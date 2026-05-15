# FoodFast catalogo-servicio V2

Cambios aplicados:

1. Se agregó Lombok en `pom.xml`.
2. Se agregó Flyway en `pom.xml`.
3. Se creó `src/main/resources/db/migration/V1__crear_tablas_catalogo.sql`.
4. Se cambió `spring.jpa.hibernate.ddl-auto=update` por `spring.jpa.hibernate.ddl-auto=validate`.
5. Se agregaron DTOs Request/Response para Categoria y Producto.
6. Se agregó `ApiResponse<T>` para respuestas uniformes.
7. Se agregó `ErrorResponse` y `GlobalExceptionHandler`.
8. Se agregaron logs con `@Slf4j` en los servicios y manejador de excepciones.
9. Se completó CRUD para categorias y productos.
10. Se agregó regla de negocio: no crear nombres duplicados y no asociar productos a categorías inactivas.

IMPORTANTE:
Si ya tenías creada la base `catalogo_db` con Hibernate `ddl-auto=update`, lo más fácil es borrar la base y dejar que Flyway la cree de nuevo.

SQL recomendado en Laragon/HeidiSQL antes de probar esta V2:

```sql
DROP DATABASE IF EXISTS catalogo_db;
CREATE DATABASE catalogo_db;
```

Después ejecuta:

```bash
mvn spring-boot:run
```

Pruebas básicas:

- GET http://localhost:8081/actuator/health
- POST http://localhost:8081/api/categorias
- GET http://localhost:8081/api/categorias
- POST http://localhost:8081/api/productos
- GET http://localhost:8081/api/productos
