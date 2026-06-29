# Sistema de Préstamos de Biblioteca Académica

Aplicación web desarrollada con **Spring Boot** para administrar el catálogo de libros de una biblioteca y controlar el ciclo completo de préstamos y devoluciones. Proyecto del curso **TPA-4002 Programación Web Back-End**.

**Integrantes:** Danny Román y Johel Gómez

## Tecnologías

- Java 21
- Spring Boot (Spring MVC, Spring Data JPA, Validation)
- Thymeleaf
- MySQL 8 (vía Docker Compose)
- Maven

## Requisitos previos

- Docker y Docker Compose
- JDK 21
- No es necesario instalar Maven: se usa el wrapper incluido (`mvnw`)

## Cómo ejecutar

### 1. Levantar la base de datos (MySQL en Docker)

Desde la raíz del proyecto:

```bash
docker compose up -d
```

Esto crea la base `biblioteca_db` y carga los datos iniciales de libros desde `docker/mysql/init.sql`.

Para reiniciar la base desde cero (borra y recarga los datos):

```bash
docker compose down -v
docker compose up -d
```

### 2. Ejecutar la aplicación

```bash
cd "Proyecto Web2 Danny-Johel"
./mvnw spring-boot:run
```

La aplicación queda disponible en: **http://localhost:8080**

> Alternativamente, se puede abrir el proyecto en IntelliJ y ejecutar la clase `ProyectoWeb2DannyJohelApplication`. Si el IDE presenta problemas de dependencias, ejecutar con `./mvnw spring-boot:run` es la opción más confiable.

## Credenciales de prueba

El sistema precarga dos usuarios al iniciar (clase `DataInitializer`). El inicio de sesión es por correo:

| Perfil | Correo | Contraseña |
|--------|--------|------------|
| Administrador | `admin@biblioteca.com` | `admin123` |
| Usuario / Estudiante | `usuario@biblioteca.com` | `usuario123` |

## Perfiles y permisos

- **ADMINISTRADOR:** gestiona el catálogo (crear, editar, activar/desactivar libros) y consulta todos los préstamos.
- **USUARIO:** consulta el catálogo, solicita préstamos, devuelve sus propios préstamos y revisa su historial.

## Endpoints REST

La API expone consultas y modificaciones (sin seguridad, según lo permitido por la especificación):

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/libros` | Lista todos los libros |
| `GET` | `/api/libros/disponibles` | Lista los libros con disponibilidad |
| `POST` | `/api/prestamos` | Crea un préstamo |
| `PATCH` | `/api/prestamos/{id}/devolver` | Registra la devolución de un préstamo |

Ejemplo de consulta:

```bash
curl http://localhost:8080/api/libros/disponibles
```

## Estructura del proyecto

```text
Proyecto/
├── Proyecto Web2 Danny-Johel/        Proyecto Spring Boot
│   └── src/main/java/web2/tec/proyectoweb2dannyjohel/
│       ├── controller/               Controladores web (@Controller)
│       ├── rest/                     Controladores REST (@RestController)
│       ├── service/                  Reglas de negocio (@Service)
│       ├── repository/               Repositorios Spring Data JPA
│       ├── entity/                   Entidades y enums del dominio
│       ├── interceptor/              AuthInterceptor (seguridad por sesión)
│       ├── config/                   WebConfig
│       └── data/                     DataInitializer (datos de prueba)
├── docker/mysql/init.sql             Carga inicial de libros
├── docker-compose.yml                Contenedor MySQL
└── docs/                             Documentación y diagramas
```

## Documentación

- `docs/Documento_Entrega_Biblioteca.pdf` — documento de requerimientos y diagramas
- `docs/diagramas/` — diagrama de componentes y diagrama de flujo request/response
