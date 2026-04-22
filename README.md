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



graph TD

%% CLIENTE
A[Cliente / Frontend] --> B[API Gateway]

%% GATEWAY A SERVICIOS
B --> C[Auth Service]
B --> D[Customer Service]
B --> E[Restaurant Service]
B --> F[Catalog Service]
B --> G[Inventory Service]
B --> H[Order Service]
B --> I[Payment Service]
B --> J[Delivery Service]
B --> K[Review Service]

%% EUREKA
C --> Z[Eureka Server]
D --> Z
E --> Z
F --> Z
G --> Z
H --> Z
I --> Z
J --> Z
K --> Z
L[Notification Service] --> Z

%% BASES DE DATOS (UNA POR SERVICIO)
C --> DB1[(Auth DB)]
D --> DB2[(Customer DB)]
E --> DB3[(Restaurant DB)]
F --> DB4[(Catalog DB)]
G --> DB5[(Inventory DB)]
H --> DB6[(Order DB)]
I --> DB7[(Payment DB)]
J --> DB8[(Delivery DB)]
K --> DB9[(Review DB)]
L --> DB10[(Notification DB)]

%% COMUNICACIÓN SINCRÓNICA (Feign)
H -->|Verifica stock| G
H -->|Consulta cliente| D
H -->|Consulta restaurante| E

%% KAFKA (EVENTOS)
I -->|Evento: Pago Exitoso| M[(Kafka)]
M --> L
M --> J

%% NOTIFICACIONES
L -->|Email / Alerta| A


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
