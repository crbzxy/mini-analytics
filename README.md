# UI Analytics Service

Microservicio REST de analítica de elementos de UI desarrollado en **Java 8** con **Spring Boot 2.7.x** y **MySQL**. Este servicio recibe eventos de interacción con elementos de UI (botones, links, cards, imágenes, etc.) desde un frontend React y los almacena en una base de datos MySQL para su posterior análisis.

**Nota:** Este proyecto fue renombrado de "Button Analytics" a "UI Analytics" para reflejar su capacidad de rastrear cualquier tipo de elemento de UI, no solo botones.

## Descripción del Proyecto

Este microservicio expone una API REST JSON que permite:

- **Registrar eventos de interacción con elementos UI**: El frontend puede enviar eventos cada vez que un usuario interactúa con cualquier elemento de UI (botón, link, card, imagen, etc.), incluyendo información sobre el tipo de cliente, la aplicación, la ruta, coordenadas, dimensiones de pantalla y metadatos adicionales.

- **Consultar estadísticas mensuales**: Obtener agregados de clicks por botón, tipo de cliente y mes para análisis y reportes.

### Conceptos Clave

- **`type`**: Tipo de cliente o canal (obligatorio). Ejemplos: `"WEB"`, `"KIOSK"`, `"MOBILE"`, `"ADMIN"`, etc. Este campo identifica el tipo de interfaz o canal desde donde se origina el evento.

- **`appId`**: Identificador dinámico propio de la empresa para la instancia o aplicación concreta (opcional). Ejemplos: `"PUESTO_001"`, `"KIOSK_CEDIS"`, `"SEPOMEX_WEB_V2"`. Este campo permite distinguir entre diferentes instancias o versiones de la misma aplicación.

- **`elementType`**: Tipo de elemento de UI con el que se interactuó (opcional). Ejemplos: `"button"`, `"link"`, `"card"`, `"image"`, `"input"`, `"dropdown"`, `"menu"`, etc. Si no se especifica, por defecto es `"button"`. Este campo permite rastrear interacciones con diferentes tipos de elementos de la interfaz, no solo botones.

## Endpoints

### Documentación Swagger/OpenAPI

La API está documentada con Swagger/OpenAPI. Accede a la documentación interactiva en:

- **Swagger UI**: http://localhost:8085/swagger-ui.html o http://localhost:8085/swagger-ui/index.html
- **API Docs (JSON)**: http://localhost:8085/api-docs
- **API Docs (YAML)**: http://localhost:8085/api-docs.yaml

### ⚠️ Autenticación

**IMPORTANTE:** El endpoint `POST /v1/events` requiere autenticación HTTP Basic.

**Credenciales por defecto:**
- Usuario: `user`
- Contraseña: `password`

**Ejemplo de uso con curl:**
```bash
curl -X POST http://localhost:8085/v1/events \
  -u user:password \
  -H "Content-Type: application/json" \
  -d '{"type":"WEB","elementId":"BTN_TEST","elementType":"button"}'
```

Los endpoints GET (lectura) son públicos y no requieren autenticación.

### POST /v1/events

Registra un evento de interacción con cualquier elemento de UI (botón, link, card, imagen, etc.). **Requiere autenticación.**

**Request Body (JSON):**
```json
{
  "type": "KIOSK",
  "appId": "PUESTO_001",
  "elementId": "BTN_PAGAR",
  "route": "/pago",
  "userId": "user123",
  "metadataJson": "{\"sessionId\": \"abc123\"}",
      "coordinateX": 150,
      "coordinateY": 300,
      "screenWidth": 1920,
      "screenHeight": 1080,
      "elementType": "button",
      "createdAt": "2024-01-15T10:30:00"
}
```

**Campos:**
- `type` (string, **obligatorio**, max 50 caracteres): Tipo de cliente/canal (ej: "WEB", "KIOSK")
- `appId` (string, opcional): Identificador de la instancia/aplicación
- `elementId` (string, **obligatorio**, max 100 caracteres): ID del elemento UI que se interactuó
- `elementType` (string, opcional, max 50 caracteres): Tipo de elemento UI (ej: "button", "link", "card", "image", "input", "dropdown"). Si no se especifica, por defecto es "button"
- `route` (string, opcional): Ruta o página donde ocurrió el evento
- `userId` (string, opcional): Identificador único del usuario que realizó el clic. Puede ser un ID de sesión, ID de usuario autenticado, o cualquier identificador que permita rastrear eventos por usuario
- `metadataJson` (string, opcional): JSON adicional como string
- `coordinateX` (integer, opcional): Coordenada X del clic en píxeles
- `coordinateY` (integer, opcional): Coordenada Y del clic en píxeles
- `screenWidth` (integer, opcional): Ancho de la pantalla en píxeles
- `screenHeight` (integer, opcional): Alto de la pantalla en píxeles
- `createdAt` (string ISO-8601, opcional): Fecha/hora del evento. Si no se envía, se usa la fecha/hora actual del servidor

**Respuestas:**
- `200 OK`: Evento guardado correctamente
  ```json
  {
    "success": true,
    "message": "Event registered successfully",
    "data": null
  }
  ```
- `400 Bad Request`: Error de validación
  ```json
  {
    "success": false,
    "error": "Validation failed: {type=type is required}"
  }
  ```
- `401 Unauthorized`: No autenticado (falta credenciales)
- `403 Forbidden`: No autorizado (credenciales inválidas)
- `500 Internal Server Error`: Error interno del servidor

### GET /api/events

Obtiene todos los eventos con paginado.

**Query Parameters:**
- `page` (int, opcional, default: 0): Número de página (0-indexed)
- `size` (int, opcional, default: 10, max: 100): Tamaño de la página

**Ejemplo de Request:**
```
GET /v1/events?page=0&size=10
```

**Nota:** Este endpoint es público y no requiere autenticación.

**Response (JSON):**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [
      {
        "id": 1,
        "type": "KIOSK",
        "appId": "PUESTO_001",
        "elementId": "BTN_PAGAR",
        "route": "/pago",
        "userId": "user123",
        "metadata": "{\"sessionId\": \"abc123\"}",
        "coordinateX": 150,
        "coordinateY": 300,
        "screenWidth": 1920,
        "screenHeight": 1080,
        "elementType": "button",
        "createdAt": "2024-01-15T10:30:00"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 150,
    "totalPages": 15,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

### GET /v1/events/{id}

Obtiene un evento específico por su ID.

**Path Parameters:**
- `id` (Long, obligatorio): ID del evento

**Ejemplo de Request:**
```
GET /v1/events/1
```

**Nota:** Este endpoint es público y no requiere autenticación.

**Respuestas:**
- `200 OK`: Evento encontrado
  ```json
  {
    "success": true,
    "message": "Operation successful",
    "data": {
      "id": 1,
      "type": "KIOSK",
      "appId": "PUESTO_001",
      "elementId": "BTN_PAGAR",
      "route": "/pago",
      "userId": "user123",
      "metadata": "{\"sessionId\": \"abc123\"}",
      "coordinateX": 150,
      "coordinateY": 300,
      "screenWidth": 1920,
      "createdAt": "2024-01-15T10:30:00"
    }
  }
  ```
- `404 Not Found`: Evento no encontrado
  ```json
  {
    "success": false,
    "error": "Event not found with id: 1"
  }
  ```

### GET /v1/stats/monthly

Obtiene estadísticas mensuales agrupadas por botón, tipo y mes.

**Response (JSON):**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": [
    {
      "elementId": "BTN_PAGAR",
      "type": "KIOSK",
      "month": "2024-01-01",
      "totalClicks": 150
    },
    {
      "elementId": "BTN_CANCELAR",
      "type": "WEB",
      "month": "2024-01-01",
      "totalClicks": 45
    }
  ]
}
```

**Campos de respuesta:**
- `elementId` (string): ID del elemento UI
- `type` (string): Tipo de cliente/canal
- `month` (string, formato YYYY-MM-01): Mes de la estadística
- `totalClicks` (number): Total de clicks en ese mes para ese elemento y tipo

**Nota:** Este endpoint es público y no requiere autenticación.

## Requisitos Previos

### Para macOS

#### 1. Docker Desktop (OBLIGATORIO)

Docker Desktop incluye Docker y Docker Compose. Es la forma más sencilla de ejecutar el proyecto.

**Instalación con Homebrew:**
```bash
brew install --cask docker
```

O descarga directamente desde: https://www.docker.com/products/docker-desktop/

**Iniciar Docker Desktop:**
- Abre Docker Desktop desde Aplicaciones
- Espera a que el ícono de Docker aparezca en la barra de menú (whale icon)
- Verifica que esté corriendo: `docker ps`

**Verificar instalación:**
```bash
docker --version
docker compose version
```

#### 2. Maven (OPCIONAL - Solo si quieres construir el JAR manualmente)

Si deseas compilar el proyecto fuera de Docker:

**Instalación con Homebrew:**
```bash
brew install maven
```

**Verificar instalación:**
```bash
mvn --version
```

#### 3. Java 8 JDK (OPCIONAL - Solo si ejecutas localmente sin Docker)

**⚠️ IMPORTANTE:** Si tienes una Mac con Apple Silicon (M1/M2/M3), **se recomienda usar Docker** en lugar de instalar Java 8 localmente, ya que Java 8 requiere Rosetta 2 (emulación x86_64) y puede ser más lento.

Si aún así quieres ejecutar el proyecto directamente en tu Mac (sin Docker):

**Para Mac con Apple Silicon (M1/M2/M3):**

**Instalación con Homebrew (requiere Rosetta 2):**
```bash
# Primero instalar Rosetta 2 (si no lo tienes)
softwareupdate --install-rosetta --agree-to-license

# Luego instalar Java 8
brew install --cask temurin@8
```

**Para Mac Intel (x86_64):**

**Instalación con Homebrew:**
```bash
brew install openjdk@8
```

**Configurar JAVA_HOME (agregar a ~/.zshrc o ~/.bash_profile):**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export PATH="$JAVA_HOME/bin:$PATH"
```

Luego recargar la configuración:
```bash
source ~/.zshrc  # o source ~/.bash_profile si usas bash
```

**Verificar instalación:**
```bash
java -version  # Debe mostrar versión 1.8.x
```

**Recomendación:** En Mac con Apple Silicon, es mejor usar Docker Compose para evitar instalar Rosetta 2 y tener mejor rendimiento.

### Resumen de Requisitos

| Herramienta | Obligatorio | Instalación macOS |
|-------------|-------------|-------------------|
| **Docker Desktop** | ✅ SÍ | `brew install --cask docker` |
| **Maven** | ❌ NO (opcional) | `brew install maven` |
| **Java 8** | ❌ NO (opcional) | Apple Silicon: `brew install --cask temurin@8`<br>Intel: `brew install openjdk@8` |

**Nota:** Si solo usas Docker Compose (recomendado, especialmente en Apple Silicon), solo necesitas Docker Desktop. Maven y Java 8 son opcionales y solo necesarios si quieres compilar/ejecutar el proyecto fuera de Docker. En Mac con Apple Silicon, Java 8 requiere Rosetta 2, por lo que Docker es la mejor opción.

## Pasos para Levantar el Proyecto

### 1. Construir el proyecto (si se ejecuta fuera de Docker)

Si deseas construir el JAR manualmente:

```bash
mvn clean package
```

Esto generará un archivo `target/ui-analytics-service-1.0.0.jar`.

### 2. Levantar con Docker Compose

Desde la raíz del proyecto, ejecuta:

```bash
docker compose up -d
```

Este comando:
- Construye la imagen del backend Spring Boot
- Levanta el contenedor de MySQL
- Levanta el contenedor del backend
- Configura la red entre ambos servicios

### 3. Verificar que los servicios están corriendo

```bash
docker compose ps
```

Deberías ver ambos servicios (`ui-analytics-service` y `mysql`) con estado `Up`.

### 4. Verificar que el backend responde

```bash
# Endpoint público (no requiere autenticación)
curl http://localhost:8085/v1/stats/monthly

# Endpoint protegido (requiere autenticación)
curl -u user:password http://localhost:8085/v1/events \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"type":"WEB","elementId":"TEST","elementType":"button"}'
```

Deberías recibir una respuesta JSON (probablemente un array vacío `[]` si no hay datos aún).

### 5. Ver logs

```bash
# Logs de ambos servicios
docker compose logs -f

# Solo logs del backend
docker compose logs -f ui-analytics-service

# Solo logs de MySQL
docker compose logs -f mysql
```

## Esquema de Puertos

- **Backend**: `http://localhost:8085`
- **MySQL**: `localhost:3306` (mapeado al host, opcional para conexiones externas)
- **Frontend React**: `http://localhost:3000` (debe estar corriendo por separado)

## Ejemplos de Uso desde el Frontend React

### Registrar un evento de clic

```typescript
async function registerUIElementClick(
  type: string,
  appId: string | null,
  elementId: string,
  elementType: string = 'button',
  route?: string
) {
  try {
    const response = await fetch('http://localhost:8085/v1/events', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + btoa('user:password') // Base64 encode de user:password
      },
      body: JSON.stringify({
        type: type,           // Ej: 'KIOSK', 'WEB'
        appId: appId,         // Ej: 'PUESTO_001' (opcional)
        elementId: elementId,   // Ej: 'BTN_PAGAR'
        elementType: 'button', // Tipo de elemento: 'button', 'link', 'card', etc.
        route: route,         // Ej: '/pago' (opcional)
        coordinateX: event.clientX,  // Coordenada X del clic (opcional)
        coordinateY: event.clientY,  // Coordenada Y del clic (opcional)
        screenWidth: window.screen.width,  // Ancho de pantalla (opcional)
        screenHeight: window.screen.height,  // Alto de pantalla (opcional)
        createdAt: new Date().toISOString(), // Opcional
      }),
    });

    if (response.ok) {
      const data = await response.json();
      console.log('Evento registrado:', data);
    } else {
      const error = await response.json();
      console.error('Error:', error);
    }
  } catch (error) {
    console.error('Error de red:', error);
  }
}

// Ejemplo de uso
registerButtonClick('KIOSK', 'PUESTO_001', 'BTN_PAGAR', '/pago');
```

### Obtener eventos con paginado

```typescript
async function getEvents(page: number = 0, size: number = 10) {
  try {
    const response = await fetch(
      `http://localhost:8085/v1/events?page=${page}&size=${size}`
    );
    const result = await response.json();
    if (result.success) {
      console.log('Eventos:', result.data.content);
      console.log('Total:', result.data.totalElements);
      console.log('Páginas:', result.data.totalPages);
      return result.data;
    }
  } catch (error) {
    console.error('Error al obtener eventos:', error);
  }
}
```

### Obtener evento por ID

```typescript
async function getEventById(id: number) {
  try {
    const response = await fetch(`http://localhost:8085/v1/events/${id}`);
    const result = await response.json();
    if (result.success) {
      console.log('Evento:', result.data);
      return result.data;
    } else {
      console.error('Error:', result.error);
    }
  } catch (error) {
    console.error('Error al obtener evento:', error);
  }
}
```

### Obtener estadísticas mensuales

```typescript
async function getMonthlyStats() {
  try {
    const response = await fetch('http://localhost:8085/v1/stats/monthly');
    const result = await response.json();
    if (result.success) {
      console.log('Estadísticas:', result.data);
      return result.data;
    }
  } catch (error) {
    console.error('Error al obtener estadísticas:', error);
  }
}
```

## Modelo de Datos

### Tabla: `ui_events`

| Campo       | Tipo          | Nullable | Descripción                                    |
|-------------|---------------|----------|------------------------------------------------|
| `id`        | BIGINT        | NO       | ID autoincremental (PK)                        |
| `type`      | VARCHAR(50)   | NO       | Tipo de cliente/canal (WEB, KIOSK, etc.)       |
| `app_id`    | VARCHAR(100)  | YES      | Identificador de instancia/aplicación           |
| `element_id` | VARCHAR(100)  | NO       | ID del elemento UI que se interactuó            |
| `element_type` | VARCHAR(50) | YES      | Tipo de elemento UI (button, link, card, etc.)   |
| `route`     | VARCHAR(255)   | YES      | Ruta o página donde ocurrió el evento           |
| `user_id`   | VARCHAR(100)  | YES      | Identificador único del usuario que realizó el clic |
| `metadata`  | TEXT          | YES      | JSON adicional como string                      |
| `coordinate_x` | INT        | YES      | Coordenada X del clic en píxeles                |
| `coordinate_y` | INT        | YES      | Coordenada Y del clic en píxeles                |
| `screen_width` | INT        | YES      | Ancho de la pantalla en píxeles                  |
| `screen_height` | INT       | YES      | Alto de la pantalla en píxeles                   |
| `created_at`| DATETIME      | NO       | Fecha y hora de creación del evento             |

### Índices

La tabla se crea automáticamente con `spring.jpa.hibernate.ddl-auto=update`. Para producción, se recomienda crear índices en:
- `type`
- `element_id`
- `created_at`
- `app_id` (si se usa frecuentemente)

## Configuración de Variables de Entorno

El proyecto usa variables de entorno para la configuración de la base de datos. Puedes modificar el archivo `docker-compose.yml` o crear un archivo `.env`:

```env
MYSQL_ROOT_PASSWORD=root_password
MYSQL_DATABASE=ui_analytics
MYSQL_USER=analytics_user
MYSQL_PASSWORD=analytics_pass
```

El backend Spring Boot lee estas variables a través de `application.yml`:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### ⚠️ Seguridad y Protección de Datos Sensibles

**IMPORTANTE:** El proyecto incluye un archivo `.gitignore` que protege datos sensibles:

- **Archivos `.env`** y variables de entorno
- **Archivos de configuración local** (`application-local.yml`, `application-prod.yml`, etc.)
- **Credenciales y certificados** (`.pem`, `.key`, `.p12`, `.jks`)
- **Logs** que puedan contener información sensible
- **Archivos de base de datos** locales

**Mejores prácticas:**
1. **NUNCA** subas archivos `.env` al repositorio
2. Usa variables de entorno o secretos gestionados (Docker Secrets, Kubernetes Secrets, etc.)
3. Para desarrollo local, crea un archivo `.env` basado en `.env.example` (si existe)
4. Revisa el `.gitignore` antes de hacer commit para asegurarte de que no se suban datos sensibles
5. Usa diferentes credenciales para desarrollo, staging y producción

## Detener los Servicios

```bash
docker compose down
```

Para eliminar también los volúmenes (y por tanto los datos):

```bash
docker compose down -v
```

## Estructura del Proyecto

```
button-analytics-service/
├── src/
│   └── main/
│       ├── java/com/miempresa/analytics/
│       │   ├── ButtonAnalyticsServiceApplication.java
│       │   ├── config/
│       │   │   └── CorsConfig.java
│       │   ├── controller/
│       │   │   └── ButtonEventController.java
│       │   ├── dto/
│       │   │   ├── ButtonEventRequest.java
│       │   │   └── ButtonMonthlyStat.java
│       │   ├── model/
│       │   │   └── ButtonEvent.java
│       │   ├── repository/
│       │   │   └── ButtonEventRepository.java
│       │   └── service/
│       │       └── ButtonEventService.java
│       └── resources/
│           └── application.yml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Tecnologías Utilizadas

- **Java 8**: Lenguaje de programación
- **Spring Boot 2.7.18**: Framework de aplicación
- **Spring Data JPA**: Persistencia de datos
- **MySQL 8.0**: Base de datos relacional
- **Maven**: Herramienta de construcción
- **Docker & Docker Compose**: Contenedorización y orquestación
- **SpringDoc OpenAPI (Swagger)**: Documentación de API
- **Bean Validation**: Validación de datos

## Buenas Prácticas Implementadas

1. **Validación de datos**: Uso de Bean Validation (`@NotBlank`, `@Size`) en DTOs
2. **Manejo global de excepciones**: `GlobalExceptionHandler` para respuestas consistentes
3. **DTOs de respuesta estándar**: `ApiResponse<T>` para respuestas uniformes
4. **Paginado**: Implementación de paginado para listas grandes
5. **Documentación API**: Swagger/OpenAPI para documentación interactiva
6. **Seguridad**: Spring Security con autenticación HTTP Basic para endpoints de modificación
7. **Versionado de API**: Prefijo `/v1` para versionado de endpoints
8. **Logging**: Uso de SLF4J para logging estructurado
9. **Separación de capas**: Controller → Service → Repository
10. **Transacciones**: Uso de `@Transactional` para operaciones de base de datos
11. **CORS configurado**: Permite llamadas desde frontend React
12. **Health checks**: Configurados en Docker para monitoreo
13. **API genérica**: Soporte para múltiples tipos de elementos UI, no solo botones

## Futuras Extensiones

### Funcionalidades Planificadas

1. **Más tipos de eventos**: Extender el modelo para soportar otros tipos de eventos además de clicks en botones (por ejemplo: cambio de pantalla, errores, tiempo de carga).

2. **Filtros en estadísticas**: Añadir parámetros de query al endpoint `/api/stats/monthly` para filtrar por:
   - `type` (tipo de cliente)
   - `appId` (instancia/aplicación)
   - Rango de fechas (desde/hasta)
   - `elementId` específico

3. **Filtros en eventos**: Añadir filtros al endpoint `/api/events` para buscar por:
   - `type`
   - `appId`
   - `elementId`
   - Rango de fechas

4. **Endpoints adicionales**:
   - Estadísticas diarias
   - Estadísticas por usuario
   - Top botones más clickeados
   - Tendencias temporales

5. **Integración con herramientas de BI**: Exportar datos a sistemas de Business Intelligence o dashboards internos.

6. **Autenticación y autorización**: Añadir seguridad con JWT o OAuth2 para proteger los endpoints.

7. **Rate limiting**: Implementar límites de tasa para prevenir abusos.

8. **Métricas y monitoreo**: Integrar con Prometheus, Grafana o herramientas similares.

9. **Cache**: Implementar cache para estadísticas frecuentemente consultadas.

## Solución de Problemas

### El backend no se conecta a MySQL

Verifica que:
1. MySQL esté corriendo: `docker compose ps`
2. Las variables de entorno estén correctas en `docker-compose.yml`
3. Los logs del backend: `docker compose logs button-analytics-service`

### Error de CORS en el frontend

Asegúrate de que:
1. El frontend esté corriendo en `http://localhost:3000`
2. La configuración de CORS en `CorsConfig.java` permita ese origen

### Puerto 8085 ya está en uso

Cambia el puerto en `application.yml` y `docker-compose.yml`, o detén el proceso que está usando el puerto 8085.

## Licencia

Este proyecto es de uso interno.

## Contacto

Para más información, contacta al equipo de desarrollo.

# mini-analytics
