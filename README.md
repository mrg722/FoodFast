# FoodFast: E-commerce de Comida Online

Sistema distribuido de gestión de pedidos y entregas de comida basado en una arquitectura de microservicios.

## Integrantes
* **Martin** - [Enlace a perfil de GitHub]
* **Damian** - [Enlace a perfil de GitHub]

## Descripción del Proyecto:
FoodFast busca resolver la alta demanda de pedidos a domicilio mediante un sistema escalable y robusto. La aplicación permite a los clientes explorar menús, realizar pedidos en tiempo real, procesar pagos seguros y calificar el servicio, todo bajo un entorno de alta disponibilidad.

## Arquitectura del Sistema
El sistema utiliza una arquitectura de microservicios desacoplados que se comunican de forma **sincrónica (OpenFeign)** y **asincrónica (Apache Kafka)**.

* **Service Discovery:** Netflix Eureka
* **API Gateway:** Spring Cloud Gateway
* **Configuración:** Bases de datos independientes por servicio (Principio de Aislamiento).


<img width="1598" height="766" alt="image" src="https://github.com/user-attachments/assets/75373b7f-25cf-4841-9088-c9ef9824d352" />



## 🛠️ Tecnologías
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.x
* **Persistencia:** Spring Data JPA / Hibernate
* **Bases de Datos:** MySQL (1 por microservicio)
* **Seguridad:** Spring Security + JJWT (JSON Web Token) + BCrypt
* **Mensajería:** Apache Kafka
* **Documentación:** SpringDoc / OpenAPI (Swagger)

##  Microservicios y Responsabilidades

1. **auth-service:** Centraliza la seguridad, gestión de usuarios, roles (Admin/Cliente) y generación de tokens JWT.
2. **customer-service:** Administra los perfiles de los clientes, direcciones de entrega y datos de contacto.
3. **catalog-service:** Gestiona el menú digital, categorías de comida, descripciones y precios de los platos.
4. **inventory-service:** Controla la disponibilidad de ingredientes o platos en tiempo real para evitar pedidos sin stock.
5. **order-service:** Orquestador central del ciclo de vida del pedido (Creación, validación y estados).
6. **payment-service:** Procesa las transacciones financieras y emite comprobantes de pago digitales.
7. **delivery-service:** Gestiona la logística de reparto, asignación de motoristas y seguimiento del envío.
8. **restaurant-service:** Administra la información de los locales físicos, horarios de apertura y cierres temporales.
9. **review-service:** Permite a los usuarios calificar y dejar comentarios sobre los platos y la calidad del servicio.
10. **notification-service:** Escucha eventos de Kafka para enviar alertas automáticas, correos de confirmación y estados del pedido.

##  Cómo Ejecutar (Próximamente)
1. Clonar el repositorio.
2. Levantar el servicio de Eureka.
3. Configurar las variables de entorno para las bases de datos MySQL.
4. Ejecutar cada microservicio con Maven/Gradle.

##  Endpoints Principales (En desarrollo)
* `POST /auth/login` - Autenticación
* `GET /catalog/menu` - Consultar platos
* `POST /orders/create` - Realizar pedido

##  Evidencias
*(Aquí subirán capturas de pantalla de Postman, diagramas DER de las bases de datos y logs de la consola a medida que avancen)*
