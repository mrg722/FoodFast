# Cambios aplicados V2 - resena-servicio

- Spring Boot 3.5.14 y Java 21.
- Lombok para reducir getters/setters manuales.
- Flyway con migración `V1__crear_tabla_resenas.sql`.
- `ddl-auto=validate` para validar contra tablas creadas por Flyway.
- ApiResponse uniforme.
- Manejo global de excepciones.
- Logs con SLF4J.
- Validaciones Bean Validation.
- Endpoints para CRUD, reseñas por producto, reseñas por cliente y promedio.
