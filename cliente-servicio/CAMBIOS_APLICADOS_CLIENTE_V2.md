# Cambios aplicados a cliente-servicio V2

- Se mantuvo Spring Boot 3.5.14, igual que el proyecto generado desde Spring Initializr.
- Se completó la estructura CSR: controller, service, repository y model.
- Se agregaron DTOs Request/Response.
- Se agregó ApiResponse para respuestas uniformes.
- Se agregaron excepciones y GlobalExceptionHandler.
- Se agregó Lombok para reducir getters, setters y constructores manuales.
- Se agregó Flyway con `V1__crear_tablas_clientes.sql`.
- Se configuró `ddl-auto=validate` para que Hibernate valide lo creado por Flyway.
- Se agregaron logs en la capa Service y en el manejador global de excepciones.
- Se agregó README.md con ejecución, endpoints, pruebas y consideración futura de JWT.

## Nota sobre versiones

El microservicio usa Spring Boot 3.5.14. Es recomendable que todos los microservicios del mismo proyecto usen la misma versión principal para evitar diferencias de dependencias y comportamientos entre servicios.
