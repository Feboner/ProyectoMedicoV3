# MS-Pacientes - Microservicio de Gestión de Pacientes

## Descripción
Microservicio responsable de la gestión integral de pacientes en el Sistema Médico DUOC. Proporciona APIs REST para crear, leer, actualizar y eliminar registros de pacientes.

## Características
- ✅ CRUD completo de pacientes
- ✅ Búsqueda por RUN (Run Único Nacional)
- ✅ Validación de datos
- ✅ Documentación automática con Swagger/OpenAPI
- ✅ Manejo de excepciones globalizado
- ✅ Logs detallados
- ✅ Dockerizado

## Tecnologías
- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA**
- **MySQL 8.0**
- **Lombok**
- **SpringDoc OpenAPI 2.3.0**

## Estructura del Proyecto

```
ms-pacientes/
├── src/
│   ├── main/
│   │   ├── java/com/duoc/ms_pacientes/
│   │   │   ├── MsPacientesApplication.java       # Clase principal
│   │   │   ├── controller/
│   │   │   │   └── PacienteController.java       # REST Controller
│   │   │   ├── service/
│   │   │   │   └── PacienteService.java          # Lógica de negocio
│   │   │   ├── repository/
│   │   │   │   └── PacienteRepository.java       # Acceso a datos
│   │   │   ├── model/
│   │   │   │   └── Paciente.java                 # Entidad JPA
│   │   │   └── config/
│   │   │       └── OpenApiConfig.java            # Configuración Swagger
│   │   └── resources/
│   │       └── application.properties            # Configuración aplicación
│   └── test/
├── pom.xml                                        # Dependencias Maven
├── Dockerfile                                     # Docker image
└── .gitignore
```

## Configuración de Base de Datos

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_medico_pacientes
spring.datasource.username=root
spring.datasource.password=duoc
spring.jpa.hibernate.ddl-auto=update
```

### Estructura de tabla
```sql
CREATE TABLE paciente (
    id INT PRIMARY KEY AUTO_INCREMENT,
    run VARCHAR(8) UNIQUE NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    created_at DATE NOT NULL,
    INDEX idx_run (run),
    INDEX idx_correo (correo)
);
```

## Endpoints

### 1. Listar todos los pacientes
```http
GET /api/pacientes
Content-Type: application/json

Response 200 OK:
[
  {
    "id": 1,
    "run": "12345678",
    "nombres": "Juan",
    "apellidos": "Pérez García",
    "fechaNacimiento": "1990-05-15",
    "correo": "juan@example.com",
    "createdAt": "2024-01-20"
  }
]
```

### 2. Obtener paciente por RUN
```http
GET /api/pacientes/rut/12345678
Content-Type: application/json

Response 200 OK:
{
  "id": 1,
  "run": "12345678",
  "nombres": "Juan",
  "apellidos": "Pérez García",
  "fechaNacimiento": "1990-05-15",
  "correo": "juan@example.com"
}
```

### 3. Obtener paciente por ID
```http
GET /api/pacientes/1
Content-Type: application/json

Response 200 OK:
{
  "id": 1,
  "run": "12345678",
  "nombres": "Juan",
  "apellidos": "Pérez García",
  "fechaNacimiento": "1990-05-15",
  "correo": "juan@example.com"
}
```

### 4. Crear nuevo paciente
```http
POST /api/pacientes
Content-Type: application/json

Request Body:
{
  "run": "12345678",
  "nombres": "Juan",
  "apellidos": "Pérez García",
  "fechaNacimiento": "1990-05-15",
  "correo": "juan@example.com"
}

Response 201 Created:
{
  "id": 1,
  "run": "12345678",
  "nombres": "Juan",
  "apellidos": "Pérez García",
  "fechaNacimiento": "1990-05-15",
  "correo": "juan@example.com",
  "createdAt": "2024-01-20"
}
```

### 5. Actualizar paciente
```http
PUT /api/pacientes/1
Content-Type: application/json

Request Body:
{
  "nombres": "Juan Carlos",
  "apellidos": "Pérez García",
  "correo": "juancarlos@example.com"
}

Response 200 OK:
{
  "id": 1,
  "run": "12345678",
  "nombres": "Juan Carlos",
  "apellidos": "Pérez García",
  "fechaNacimiento": "1990-05-15",
  "correo": "juancarlos@example.com"
}
```

### 6. Eliminar paciente
```http
DELETE /api/pacientes/1
Content-Type: application/json

Response 200 OK:
{
  "mensaje": "Paciente eliminado exitosamente",
  "id": "1"
}
```

## Compilación y Ejecución

### 1. Compilar con Maven
```bash
cd ms-pacientes
mvn clean package
```

### 2. Ejecutar localmente
```bash
mvn spring-boot:run
```

### 3. Ejecutar JAR generado
```bash
java -jar target/ms-pacientes-0.0.1-SNAPSHOT.jar
```

### 4. Con Docker
```bash
# Compilar y construir imagen
docker build -t ms-pacientes:1.0 .

# Ejecutar contenedor
docker run -p 8083:8083 \
  -e DB_HOST=host.docker.internal \
  -e DB_USER=root \
  -e DB_PASSWORD=duoc \
  ms-pacientes:1.0
```

## Documentación Interactiva

Una vez que la aplicación esté corriendo, accede a:

- **Swagger UI**: http://localhost:8083/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8083/v3/api-docs
- **Actuator Health**: http://localhost:8083/actuator/health

## Validaciones

- **RUN**: No puede estar vacío, máximo 8 dígitos, único
- **Nombres**: No puede estar vacío, máximo 100 caracteres
- **Apellidos**: No puede estar vacío, máximo 100 caracteres
- **Fecha Nacimiento**: No puede ser nula
- **Correo**: Debe ser un email válido y único

## Manejo de Errores

### Errores comunes

| Status | Descripción | Ejemplo |
|--------|-------------|---------|
| 200 | OK - Operación exitosa | GET exitoso |
| 201 | Created - Recurso creado | POST exitoso |
| 204 | No Content - Lista vacía | GET sin resultados |
| 400 | Bad Request - Datos inválidos | RUN duplicado |
| 404 | Not Found - Recurso no existe | ID inexistente |
| 409 | Conflict - RUN/Email duplicado | RUN ya registrado |
| 500 | Server Error | Error interno |

## Logs

Los logs se guardan con diferentes niveles:
- `DEBUG`: Para desarrollo y debugging
- `INFO`: Información general de operaciones
- `WARN`: Advertencias (RUT duplicado, etc.)
- `ERROR`: Errores de operación

## Próximas Mejoras

- [ ] Integración con JWT para autenticación
- [ ] Añadir rol-based access control (RBAC)
- [ ] Integración con ms-eureka para service discovery
- [ ] Caché con Redis
- [ ] Paginación en listados
- [ ] Filtros avanzados de búsqueda
- [ ] Auditoría de cambios

---

**Autor**: Equipo de Desarrollo DUOC  
**Versión**: 1.0.0  
**Fecha**: 2024-01-20
