# Memoria Técnica — Persona 2: Catálogo de coches base y pantalla de listado

## Funcionalidades implementadas

- Catálogo de coches base F1 con tarjetas visuales.
- Filtrado por equipo y búsqueda por nombre/equipo/temporada.
- Estados de carga (skeleton animado), error controlado y lista vacía.
- Navegación al detalle del coche (`/coches/:id`) protegida por guard de autenticación.
- Carga de datos iniciales automática al arrancar el backend (6 coches F1 base).

---

## Backend

### Endpoints implementados

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/coches` | Lista todos los coches |
| `GET` | `/api/coches/base` | Lista solo los coches base (`esBase=true`) |
| `GET` | `/api/coches/{id}` | Obtiene un coche por su UUID |
| `GET` | `/api/coches/usuario/{usuarioId}` | Coches de un usuario concreto |
| `GET` | `/api/coches/equipo/{equipoF1}` | Coches de un equipo (case-insensitive) |
| `POST` | `/api/coches` | Crea un coche nuevo |
| `PUT` | `/api/coches/{id}` | Actualiza un coche existente |
| `DELETE` | `/api/coches/{id}` | Elimina un coche |

### Cómo se cargan los coches base

Al arrancar Spring Boot se ejecuta `CocheDataInitializer` (implementa `CommandLineRunner`, orden 2). Comprueba si ya hay coches base con `findByEsBaseTrue()`. Si la lista está vacía, inserta los 6 coches. Esto garantiza que la operación es idempotente: si la base de datos ya tiene datos, no vuelve a insertar.

Datos iniciales insertados:

| Nomenclatura | Equipo | Temporada | Precio base |
|---|---|---|---|
| SF-23 | Ferrari | 2023 | 285.000 € |
| RB19 | Red Bull Racing | 2023 | 310.000 € |
| W14 | Mercedes-AMG | 2023 | 295.000 € |
| MCL60 | McLaren | 2023 | 265.000 € |
| AMR23 | Aston Martin | 2023 | 275.000 € |
| A523 | Alpine | 2023 | 250.000 € |

Para un coche base, `precioTotal` se inicializa igual que `precioBase`. Cuando Persona 4 añada piezas, `precioTotal` se recalculará sumando el precio de cada pieza multiplicado por su cantidad.

### DTOs

**`CocheRequest`** (entrada):
```json
{
  "nomenclatura": "SF-23",
  "equipoF1": "Ferrari",
  "temporada": "2023",
  "descripcion": "...",
  "precioBase": 285000,
  "imagenUrl": "https://...",
  "esBase": true,
  "usuarioId": null
}
```

**`CocheResponse`** (salida):
```json
{
  "id": "uuid",
  "nomenclatura": "SF-23",
  "equipoF1": "Ferrari",
  "temporada": "2023",
  "descripcion": "...",
  "precioBase": 285000,
  "precioTotal": 285000,
  "imagenUrl": "https://...",
  "esBase": true,
  "usuarioId": null,
  "createdAt": "2026-06-13T..."
}
```

Los DTOs evitan exponer directamente la entidad JPA y sus relaciones con `@JsonIgnore`. El servicio mapea entre entidad y DTO.

### Pruebas manuales

```bash
# Listar coches base
curl http://localhost:8080/api/coches/base

# Listar todos los coches
curl http://localhost:8080/api/coches

# Obtener un coche por id
curl http://localhost:8080/api/coches/{id}

# Crear un coche base
curl -X POST http://localhost:8080/api/coches \
  -H "Content-Type: application/json" \
  -d '{"nomenclatura":"SF-23","equipoF1":"Ferrari","temporada":"2023","precioBase":285000,"esBase":true}'
```

---

## Frontend Ionic-Angular

### Archivos creados o modificados

| Archivo | Tipo | Descripción |
|---|---|---|
| `core/models/car.model.ts` | Modelo | Interfaz TypeScript para `CocheResponse` |
| `core/services/car.service.ts` | Servicio | HTTP calls al backend |
| `home/home.page.ts` | Componente | Lógica del catálogo |
| `home/home.page.html` | Template | Tarjetas, filtros, buscador y estados |
| `home/home.page.scss` | Estilos | Diseño siguiendo paleta Figma |
| `pages/car-detail/` | Página | Placeholder de detalle (`/coches/:id`) |
| `app-routing.module.ts` | Routing | Ruta `/coches/:id` con guard |

### Cómo se cargan los coches en el catálogo

Al entrar en `/catalogo` se ejecuta `ionViewWillEnter()` que llama a `CarService.getBaseCars()` (`GET /api/coches/base`). La respuesta puebla el array `cars`. Los filtros activos se aplican localmente sobre ese array sin volver a llamar al backend.

### Filtros implementados

- **Por equipo:** chips de botones generados a partir de los equipos únicos de los coches recibidos. Seleccionar el mismo chip lo deselecciona (toggle).
- **Por búsqueda:** barra de búsqueda con debounce de 300 ms que filtra por `nomenclatura`, `equipoF1` y `temporada`.
- Los dos filtros son aditivos (se aplican a la vez).

### Estados visuales

| Estado | Comportamiento |
|---|---|
| Carga | Grid de 6 tarjetas skeleton con animación shimmer |
| Error backend | Componente `app-empty-state` con mensaje de error controlado |
| Sin resultados | `app-empty-state` indicando que no hay coches con ese filtro |
| Normal | Grid de tarjetas con hover, transición y flecha de navegación |

### Diseño (Figma)

Se sigue la paleta del archivo *Tienda de F1 - Grupo V*:

- Fondo: `#09090b` con gradiente radial rojo (`#e10600`) y cian (`#29dec9`)
- Tarjetas: `rgba(24,24,27,0.72)` con borde `rgba(228,225,238,0.10)`
- Equipo (kicker): `#29dec9` mayúsculas 11 px weight 900
- Nombre del coche: `#ffffff` Space Grotesk 24 px weight 900 uppercase
- Precio: `#e10600` Space Grotesk 20 px weight 900
- Hover: borde `rgba(225,6,0,0.5)` y `translateY(-2px)`
- Fuente principal: Space Grotesk (cargada vía Google Fonts en `global.scss`)

### Responsive

| Breakpoint | Columnas |
|---|---|
| < 600 px | 1 columna |
| 600–1200 px | `auto-fill` mínimo 300 px |
| ≥ 1200 px | 3 columnas fijas |

---

## Problemas encontrados y soluciones

### Imágenes

Los coches base usan URLs de `placehold.co` como placeholder, ya que no hay imágenes reales de los coches en el repositorio. En producción se sustituirían por URLs de imágenes reales o assets locales. Se añadió un handler `(error)` en la etiqueta `<img>` para mostrar `assets/shapes.svg` si la imagen no carga.

### Datos iniciales

El `DataInitializer` comprueba si ya existen coches base antes de insertar, por lo que es seguro arrancar el backend varias veces sin duplicar datos. Si se cambia el esquema y se borra la base de datos, los datos se vuelven a insertar automáticamente al arrancar.

### CORS

La configuración CORS ya estaba implementada por Persona 1 (`CorsConfig.java`), permitiendo peticiones desde `http://localhost:8100`.

### Relación `usuario` en el repositorio

Spring Data JPA resuelve `findByUsuarioId(UUID)` navegando la relación `@ManyToOne usuario` hasta su campo `id`. No fue necesario usar el separador explícito `findByUsuario_Id`.

---

## Metodología

Implementación vertical directa siguiendo el reparto: primero backend (DTO → repositorio → servicio → controlador → datos iniciales), luego frontend (modelo → servicio → template → estilos). Se probó la consistencia con el código existente de Persona 1 para mantener el mismo patrón de DTOs, excepciones y estilos.

---

## Tiempo empleado

| Tarea | Tiempo estimado |
|---|---|
| Backend (DTOs, repositorio, servicio, controlador) | ~1 h |
| Datos iniciales (6 coches F1) | ~15 min |
| Frontend (modelo, servicio, catálogo completo) | ~1.5 h |
| Estilos Figma y responsive | ~45 min |
| Página placeholder detalle y routing | ~20 min |
| **Total** | **~3 h 50 min** |

---

# Memoria Técnica - Persona 7: Reseñas, valoraciones y perfil del usuario

## Sistema de puntuacion

- Se implementa un sistema de valoracion de 1 a 5 estrellas por coche.
- Cada reseña guarda: usuario, coche, puntuacion, comentario y fecha de creacion.
- Un usuario solo puede publicar una reseña por coche. Si intenta duplicar, el backend responde `400` con mensaje controlado.

## Validaciones implementadas

### Backend (DTO + servicio)

- `puntuacion` obligatoria y entre 1 y 5.
- `comentario` obligatorio, con longitud minima de 3 y maxima de 500.
- En creacion/edicion de reseñas:
  - validacion de existencia de usuario.
  - validacion de existencia de coche.
  - bloqueo de duplicados por par `(usuarioId, cocheId)`.

### Frontend (feedback visual)

- En detalle de coche, el formulario valida comentario minimo de 3 caracteres antes de enviar.
- Se muestran errores de API en pantalla (incluyendo duplicado de reseña).
- Se muestra confirmacion al publicar correctamente.

## Endpoints de reseñas

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/resenias` | Lista todas las reseñas |
| `GET` | `/api/resenias/{id}` | Obtiene una reseña por id |
| `GET` | `/api/resenias/coche/{cocheId}` | Lista reseñas de un coche |
| `GET` | `/api/resenias/usuario/{usuarioId}` | Lista reseñas de un usuario |
| `GET` | `/api/resenias/coche/{cocheId}/resumen` | Devuelve media y total de reseñas del coche |
| `POST` | `/api/resenias` | Crea una reseña (`201`) |
| `PUT` | `/api/resenias/{id}` | Actualiza una reseña |
| `DELETE` | `/api/resenias/{id}` | Elimina una reseña (`204`) |

También se completó perfil reutilizando:

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/usuarios/{id}` | Devuelve datos de perfil |
| `PUT` | `/api/usuarios/{id}` | Edita nombre, email y password (password opcional) |

## Pantalla de perfil

- Ruta protegida: `/perfil`.
- Muestra nombre, email y rol del usuario actual.
- Permite editar nombre y email, con cambio opcional de password.
- Lista las reseñas del usuario con coche, estrellas, comentario y fecha.
- Incluye boton de cierre de sesion.

## Integracion en detalle de coche

- Se añadió bloque de reseñas en `/coches/:id` con:
  - media del coche.
  - total de reseñas.
  - listado de comentarios y estrellas.
  - formulario de publicacion para rol `CLIENTE`.
- Estilo alineado con paleta y tipografia de Figma (fondo oscuro, acentos `#e10600` y `#29dec9`, tarjetas y bordes suaves).

## Archivos clave modificados/creados

### Backend

- `backend/src/main/java/com/gcs/backend/dto/ReseniaRequest.java`
- `backend/src/main/java/com/gcs/backend/dto/ReseniaResponse.java`
- `backend/src/main/java/com/gcs/backend/dto/ResumenReseniasResponse.java`
- `backend/src/main/java/com/gcs/backend/dto/UsuarioPerfilUpdateRequest.java`
- `backend/src/main/java/com/gcs/backend/repository/ReseniaRepository.java`
- `backend/src/main/java/com/gcs/backend/service/ReseniaService.java`
- `backend/src/main/java/com/gcs/backend/controller/ReseniaController.java`
- `backend/src/main/java/com/gcs/backend/service/UsuarioService.java`
- `backend/src/main/java/com/gcs/backend/controller/UsuarioController.java`

### Frontend

- `frontend/src/app/core/models/review.model.ts`
- `frontend/src/app/core/models/review-summary.model.ts`
- `frontend/src/app/core/services/review.service.ts`
- `frontend/src/app/core/services/auth.service.ts`
- `frontend/src/app/pages/car-detail/car-detail.page.ts`
- `frontend/src/app/pages/car-detail/car-detail.page.html`
- `frontend/src/app/pages/car-detail/car-detail.page.scss`
- `frontend/src/app/pages/car-detail/car-detail.module.ts`
- `frontend/src/app/pages/profile/profile-routing.module.ts`
- `frontend/src/app/pages/profile/profile.module.ts`
- `frontend/src/app/pages/profile/profile.page.ts`
- `frontend/src/app/pages/profile/profile.page.html`
- `frontend/src/app/pages/profile/profile.page.scss`
- `frontend/src/app/app-routing.module.ts`
- `frontend/src/app/shared/components/app-header/app-header.component.ts`
- `frontend/src/app/shared/components/app-header/app-header.component.html`

## Tiempo empleado

| Tarea | Tiempo estimado |
|---|---|
| Backend reseñas (DTO, repositorio, servicio, controlador) | ~1 h 30 min |
| Endpoint de perfil (`PUT /usuarios/{id}`) | ~25 min |
| Frontend reseñas en detalle + formulario | ~1 h 20 min |
| Pantalla perfil + integracion de reseñas de usuario | ~1 h 10 min |
| Ajustes visuales Figma + pruebas de compilacion | ~35 min |
| **Total Persona 7** | **~5 h** |
