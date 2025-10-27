# Sistema AsistCar
## 🎯 Propósito
El propósito de esta plataforma es construir un **ecosistema de confianza** para la 
industria automotriz. Creamos un historial digital inmutable y trazable para cada vehículo
, registrando cada mantenimiento, reparación y actualización de kilometraje directamente desde talleres verificados. 
Al dar a los dueños el control total para aprobar reparaciones y compartir de forma segura este historial, eliminamos 
la manipulación de datos y el fraude de kilometraje. La misión de este sistema es aportar transparencia total al 
mercado de vehículos de segunda mano, protegiendo la inversión del comprador y revalorizando el mantenimiento 
honesto del dueño.
## 🚀 Tecnologias a utilizar
 
* Java 17
* Spring Boot 3.2.0
* Spring Security 6
* JWT (JJWT 0.12.3)
* PostgreSQL
* MapStruct 1.5.5
* Lombok
* Maven

## 🏗️ Arquitectura
Arquitectura Hexagonal (Ports & Adapters) dividiendo cada funcionalidad 
del sistema en 3 subcarpetas: Application, Domain e Infraestructure

## 🔐 Sistema de Autenticación JWT + OAuth2
Sistema robusto de autenticación y autorización implementado con Spring Boot 3, JWT, OAuth2 y PostgreSQL, 
siguiendo Arquitectura Hexagonal y las mejores prácticas de seguridad OWASP.

```text
📦 Módulo AUTH
├── 🎯 Application (Casos de Uso)
│   ├── Ports IN: AuthenticationUseCase, RefreshTokenUseCase
│   ├── Ports OUT: UserRepositoryPort, TokenRepositoryPort
│   ├── Services: AuthenticationService
│   └── DTOs: LoginRequest, AuthResponse
├── 💼 Domain (Lógica de Negocio)
│   └── Models: Token, User, SecurityUser
└── 🔌 Infrastructure (Adaptadores)
    ├── Adapters IN: AuthController
    ├── Adapters OUT: UserRepositoryAdapter, TokenRepositoryAdapter
    ├── Persistence: JPA Entities, Repositories
    └── Mappers: MapStruct mappers

📦 Módulo SECURITY
├── 🎯 Application
│   ├── Ports: JwtService
│   └── Services: JwtServiceImpl
└── 🔌 Infrastructure
    ├── Config: SecurityConfig, JwtProperties
    ├── Filters: JwtAuthenticationFilter
    └── Handlers: CustomAuthenticationEntryPoint, CustomAccessDeniedHandler
```
