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
