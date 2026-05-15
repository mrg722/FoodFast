# FoodFast inventario-servicio V2

Cambios aplicados:

1. Se agregó Lombok en `pom.xml`.
2. Se agregó Flyway en `pom.xml`.
3. Se creó `src/main/resources/db/migration/V1__crear_tabla_inventarios.sql`.
4. Se cambió `spring.jpa.hibernate.ddl-auto=update` por `spring.jpa.hibernate.ddl-auto=validate`.
5. Se corrigió el paquete `exeption` a `exception`.
6. Se agregó `ApiResponse<T>` para respuestas uniformes.
7. Se agregó `ErrorResponse` y `GlobalExceptionHandler`.
8. Se agregaron logs con `@Slf4j` en servicio y manejador de excepciones.
9. Se mantiene CRUD completo.
10. Se mantienen reglas de negocio: no permitir stock negativo, consultar stock por productoId y descontar stock sin permitir stock insuficiente.
11. Se dejaron rutas alias para evitar confusión:
    - GET `/api/inventarios/producto/{productoId}/stock`
    - GET `/api/inventarios/stock/{productoId}`
    - PUT `/api/inventarios/descontar-stock`
    - POST `/api/inventarios/descontar`

IMPORTANTE:
Si ya tenías creada la base `inventario_db` con Hibernate `ddl-auto=update`, lo más fácil es borrar la base y dejar que Flyway la cree de nuevo.

SQL recomendado en Laragon/HeidiSQL antes de probar esta V2:

```sql
DROP DATABASE IF EXISTS inventario_db;
CREATE DATABASE inventario_db;
```

Después ejecuta:

```bash
mvn spring-boot:run
```

Pruebas básicas:

- GET http://localhost:8082/actuator/health
- POST http://localhost:8082/api/inventarios
- GET http://localhost:8082/api/inventarios
- GET http://localhost:8082/api/inventarios/producto/1/stock
- PUT http://localhost:8082/api/inventarios/descontar-stock
