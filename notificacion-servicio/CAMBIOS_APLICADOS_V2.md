# Cambios aplicados V2 - notificacion-servicio

- Spring Boot 3.5.14 y Java 21.
- Lombok para reducir getters/setters manuales.
- Flyway con migración `V1__crear_tabla_notificaciones.sql`.
- `ddl-auto=validate` para validar contra tablas creadas por Flyway.
- ApiResponse uniforme.
- Manejo global de excepciones.
- Logs con SLF4J.
- Validaciones Bean Validation.
- Enums para tipo, canal y estado de notificación.
- Endpoints para CRUD, envío simulado y marcar como leída.
