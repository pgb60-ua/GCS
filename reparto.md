# Reparto de trabajo GCS

El orden de trabajo recomendado es:

1. Completar y probar backend.
2. Crear/implementar el frontend nativo con Ionic-Angular siguiendo el diseÃ±o de Figma.
3. Integrar frontend con backend.
4. Preparar demo, memoria tÃ©cnica y `autores.txt`.

## Estado actual del backend

En `GCS/backend/` ya existe un proyecto Spring Boot con:

- Modelos JPA para todas las tablas del esquema del `README.md`: `Usuario`, `Coche`, `Pieza`, `Aerodinamica`, `Motor`, `Pintura`, `Suspension`, `CochePieza`, `SolicitudPieza`, `Venta` y `Resenia`.
- Repositorios JPA para todas esas entidades.
- Servicios bÃ¡sicos para todas esas entidades.
- Controladores REST con CRUD bÃ¡sico para todas esas entidades.
- ConfiguraciÃ³n de PostgreSQL en `application.properties`.
- `docker-compose.yml` para levantar PostgreSQL.

Lo que falta o conviene completar antes de empezar el frontend:

- DTOs de entrada y salida. Ahora los controladores reciben y devuelven entidades directamente, y muchas relaciones tienen `@JsonIgnore`, asÃ­ que desde Ionic no se verÃ¡n ids relacionados como `usuarioId`, `cocheId` o `piezaId`.
- Endpoints especÃ­ficos para flujos reales, no solo CRUD genÃ©rico. Por ejemplo: piezas por tipo, coches base, coches de un usuario, piezas de un coche, solicitudes de un usuario, aprobar/rechazar solicitud, crear venta desde coche, reseÃ±as de un coche.
- Validaciones con `@Valid`, `@NotBlank`, `@Email`, `@Positive`, rangos de puntuaciÃ³n, etc.
- Manejo de errores uniforme para devolver `400`, `404` y mensajes claros.
- CORS para permitir peticiones desde Ionic (`http://localhost:8100`).
- Datos iniciales de prueba: usuarios, coches base, piezas de catÃ¡logo, reseÃ±as y solicitudes.
- AutenticaciÃ³n real o simulada. Para la demo basta con login simulado o bÃ¡sico, pero hay que evitar que el frontend dependa de escribir UUIDs a mano.
- Tests de servicios/controladores o al menos pruebas manuales documentadas con `curl`/Postman.
- DocumentaciÃ³n de endpoints en README o archivo propio.

---

# Persona 1 - Base tÃ©cnica, usuarios, login y estructura de app

## Backend

1. Revisar `backend/src/main/java/com/gcs/backend/model/Usuario.java`.
2. AÃ±adir validaciones mÃ­nimas:
   - `nombre`: obligatorio.
   - `email`: obligatorio y formato email.
   - `password`: obligatorio.
   - `rol`: obligatorio, con valores esperados `CLIENTE` o `ADMIN`.
3. Crear DTOs para usuario:
   - `UsuarioRequest`: `nombre`, `email`, `password`, `rol`.
   - `UsuarioResponse`: `id`, `nombre`, `email`, `rol`, `createdAt`.
   - `LoginRequest`: `email`, `password`.
   - `LoginResponse`: `id`, `nombre`, `email`, `rol`.
4. Modificar `UsuarioRepository` para aÃ±adir:
   - `Optional<Usuario> findByEmail(String email);`
   - `boolean existsByEmail(String email);`
5. Modificar `UsuarioService` para incluir:
   - Crear usuario validando que el email no exista.
   - Login simple comparando email y password.
   - Listar usuarios sin devolver password.
   - Obtener usuario por id sin devolver password.
6. Modificar `UsuarioController`:
   - `GET /api/usuarios`
   - `GET /api/usuarios/{id}`
   - `POST /api/usuarios`
   - `POST /api/auth/login`
   - `PUT /api/usuarios/{id}`
   - `DELETE /api/usuarios/{id}`
7. Crear configuraciÃ³n CORS:
   - Permitir `http://localhost:8100`.
   - Permitir mÃ©todos `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, `OPTIONS`.
   - Permitir cabeceras normales de JSON.
8. Crear manejador global de errores:
   - `ResourceNotFoundException`.
   - `BadRequestException`.
   - `GlobalExceptionHandler` con respuestas JSON del tipo `{ "message": "..." }`.
9. Probar con peticiones:

```bash
curl -X POST http://localhost:8080/api/usuarios -H "Content-Type: application/json" -d "{\"nombre\":\"Admin\",\"email\":\"admin@gcs.com\",\"password\":\"1234\",\"rol\":\"ADMIN\"}"
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"email\":\"admin@gcs.com\",\"password\":\"1234\"}"
curl http://localhost:8080/api/usuarios
```

## Frontend Ionic-Angular

1. Crear la app Ionic si todavÃ­a no existe:

```bash
cd GCS
ionic start frontend blank --type=angular
```

2. Crear estructura base:
   - `src/app/core/models/`
   - `src/app/core/services/`
   - `src/app/core/interceptors/` si se decide aÃ±adir interceptor.
   - `src/app/pages/login/`
   - `src/app/pages/tabs/` o layout equivalente segÃºn Figma.
3. Crear `environment.ts` con:

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

4. Crear modelos:
   - `user.model.ts`
   - `login-request.model.ts`
   - `login-response.model.ts`
5. Crear `AuthService`:
   - `login(email: string, password: string)`.
   - `logout()`.
   - `getCurrentUser()`.
   - Guardar usuario en `localStorage`.
   - Exponer si el usuario es `ADMIN` o `CLIENTE`.
6. Crear pantalla de login segÃºn Figma:
   - Inputs email/password.
   - BotÃ³n principal para entrar.
   - Mensaje de error si falla.
   - Estado de carga mientras se llama al backend.
7. Crear pantalla de registro de cliente:
   - Inputs nombre/email/password/confirmar password.
   - Crear usuario con rol `CLIENTE` usando `POST /api/usuarios`.
   - Mensaje de error si el email ya existe o la validaciÃ³n falla.
   - Guardar sesiÃ³n local tras registrarse correctamente.
8. Crear navegaciÃ³n base:
   - Si login correcto, ir a catÃ¡logo/home.
   - Si no hay sesiÃ³n, volver a login.
   - MenÃº o tabs segÃºn el diseÃ±o de Figma.
9. Crear componentes compartidos bÃ¡sicos:
   - Cabecera con logo/nombre.
   - BotÃ³n de cerrar sesiÃ³n.
   - Componente de estado vacÃ­o.
   - Componente de spinner/carga.
10. Prueba final:
   - Levantar backend.
   - Ejecutar `ionic serve`.
   - Entrar con usuario creado por backend.
   - Crear un usuario cliente desde registro.
   - Refrescar navegador y comprobar que mantiene sesiÃ³n.
   - Cerrar sesiÃ³n y comprobar que vuelve a login.

## Memoria

Documentar:

- CÃ³mo se ha implementado login.
- CÃ³mo se ha implementado registro de cliente.
- Por quÃ© se usa sesiÃ³n local simulada.
- Endpoints de usuario/auth.
- Problemas encontrados con CORS o conexiÃ³n Ionic-backend.
- Tiempo empleado.

---

# Persona 2 - CatÃ¡logo de coches base y pantalla de listado

## Backend

1. Revisar `Coche`, `CocheRepository`, `CocheService` y `CocheController`.
2. Crear DTOs:
   - `CocheRequest`: `nomenclatura`, `equipoF1`, `temporada`, `descripcion`, `precioBase`, `imagenUrl`, `esBase`, `usuarioId`.
   - `CocheResponse`: `id`, `nomenclatura`, `equipoF1`, `temporada`, `descripcion`, `precioBase`, `precioTotal`, `imagenUrl`, `esBase`, `usuarioId`, `createdAt`.
3. AÃ±adir validaciones:
   - `nomenclatura`: obligatoria.
   - `equipoF1`: obligatorio.
   - `temporada`: obligatoria.
   - `precioBase`: positivo o cero.
   - `precioTotal`: positivo o cero.
4. AÃ±adir mÃ©todos en `CocheRepository`:
   - `List<Coche> findByEsBaseTrue();`
   - `List<Coche> findByUsuarioId(UUID usuarioId);`
   - `List<Coche> findByEquipoF1IgnoreCase(String equipoF1);`
5. Modificar `CocheService`:
   - Al crear coche base, `precioTotal = precioBase`.
   - Si se crea coche de usuario, asociar `usuarioId`.
   - Si `usuarioId` no existe, devolver `404`.
   - Mapear entidades a DTO para que el frontend reciba ids.
6. Modificar `CocheController`:
   - `GET /api/coches`
   - `GET /api/coches/base`
   - `GET /api/coches/usuario/{usuarioId}`
   - `GET /api/coches/equipo/{equipoF1}`
   - `GET /api/coches/{id}`
   - `POST /api/coches`
   - `PUT /api/coches/{id}`
   - `DELETE /api/coches/{id}`
7. AÃ±adir datos iniciales de coches base:
   - Al menos 6 coches de F1.
   - Cada coche debe tener `nomenclatura`, `equipoF1`, `temporada`, `descripcion`, `precioBase`, `precioTotal`, `imagenUrl`, `esBase=true`.
   - Puede hacerse con `data.sql` o `CommandLineRunner`.
8. Probar:

```bash
curl http://localhost:8080/api/coches/base
curl http://localhost:8080/api/coches
```

## Frontend Ionic-Angular

1. Crear modelo `car.model.ts` con los campos del `CocheResponse`.
2. Crear `CarService`:
   - `getBaseCars()`.
   - `getAllCars()`.
   - `getCarById(id: string)`.
   - `getCarsByUser(userId: string)`.
3. Crear pantalla de catÃ¡logo/home:
   - Ruta sugerida: `/catalogo`.
   - Mostrar tarjetas de coches base siguiendo Figma.
   - Cada tarjeta debe mostrar imagen, nomenclatura, equipo, temporada y precio.
   - AÃ±adir filtro visual por equipo o buscador si estÃ¡ en Figma.
   - Estado de carga.
   - Estado vacÃ­o si no llegan coches.
   - Estado de error si falla backend.
4. Conectar tarjeta con detalle:
   - Al pulsar tarjeta, navegar a `/coches/:id`.
5. Crear datos mock temporales solo si backend no estÃ¡ levantado, pero dejar por defecto llamada HTTP real.
6. Revisar responsive:
   - En mÃ³vil: tarjetas en una columna.
   - En tablet/escritorio: grid de dos o mÃ¡s columnas si Ionic lo permite.
7. Prueba final:
   - Backend con datos iniciales.
   - `ionic serve`.
   - Ver catÃ¡logo.
   - Abrir un coche.
   - Comprobar que no se rompe si backend estÃ¡ apagado: debe mostrar error controlado.

## Memoria

Documentar:

- Endpoints usados para catÃ¡logo.
- CÃ³mo se cargan coches base.
- Capturas del catÃ¡logo.
- Problemas de imÃ¡genes o datos iniciales.
- Tiempo empleado.

---

# Persona 3 - Javier - Detalle de coche, duplicar coche base y garaje del usuario

## Backend

1. Revisar la entidad `Coche` y su relaciÃ³n con `Usuario`.
2. Crear endpoint de duplicado/personalizaciÃ³n inicial:
   - `POST /api/coches/{cocheBaseId}/duplicar`
   - Body: `{ "usuarioId": "..." }`
   - Debe crear un coche nuevo para el usuario copiando datos del coche base.
   - El coche nuevo debe tener `esBase=false`.
   - `precioTotal` debe empezar igual que `precioBase`.
3. AÃ±adir endpoint de actualizaciÃ³n parcial de coche personalizado:
   - `PATCH /api/coches/{id}`
   - Permitir cambiar `nomenclatura`, `descripcion`, `imagenUrl` si aplica.
   - No permitir cambiar el `id`.
4. AÃ±adir endpoint:
   - `GET /api/coches/{id}/resumen`
   - Debe devolver datos del coche, piezas asociadas y precio calculado.
5. Coordinar con P4 para que el cÃ¡lculo de precio total incluya piezas:
   - `precioTotal = precioBase + sum(precio pieza * cantidad)`.
6. AÃ±adir validaciÃ³n:
   - Solo coches no base deberÃ­an modificarse desde personalizaciÃ³n.
   - Si se intenta modificar un coche base, devolver error claro.
7. Probar:

```bash
curl -X POST http://localhost:8080/api/coches/ID_COCHE_BASE/duplicar -H "Content-Type: application/json" -d "{\"usuarioId\":\"ID_USUARIO\"}"
curl http://localhost:8080/api/coches/usuario/ID_USUARIO
curl http://localhost:8080/api/coches/ID_COCHE/resumen
```

## Frontend Ionic-Angular

1. Crear pantalla detalle de coche:
   - Ruta: `/coches/:id`.
   - Mostrar imagen principal, nombre, equipo, temporada, descripciÃ³n y precio base.
   - BotÃ³n principal: `Personalizar` o el texto exacto de Figma.
2. Al pulsar personalizar:
   - Si no hay usuario logueado, volver a login.
   - Si hay usuario, llamar a `POST /api/coches/{cocheBaseId}/duplicar`.
   - Navegar a `/personalizar/:idNuevo`.
3. Crear pantalla `Mi garaje`:
   - Ruta: `/garaje`.
   - Mostrar coches del usuario actual.
   - Cada coche muestra nombre, precio total, fecha y estado visual.
   - BotÃ³n para continuar editando.
4. Crear mÃ©todos en `CarService`:
   - `duplicateBaseCar(baseCarId: string, userId: string)`.
   - `getCarSummary(id: string)`.
   - `patchCar(id: string, payload: Partial<Car>)`.
5. Crear pantalla base de personalizaciÃ³n:
   - Ruta: `/personalizar/:id`.
   - Cargar resumen del coche.
   - Mostrar precio total.
   - Dejar secciones preparadas para piezas de P4.
6. Prueba final:
   - Login.
   - Entrar a catÃ¡logo.
   - Abrir coche.
   - Pulsar personalizar.
   - Ver coche creado en `Mi garaje`.
   - Recargar navegador y comprobar que el coche sigue en backend.

## Memoria

Documentar:

- Flujo de duplicado de coche base.
- Diferencia entre coche base y coche personalizado.
- Endpoints usados por detalle, personalizaciÃ³n y garaje.
- Tiempo empleado.

---

# Persona 4 - Piezas, categorÃ­as tÃ©cnicas y configurador

## Backend

1. Revisar entidades:
   - `Pieza`
   - `Aerodinamica`
   - `Motor`
   - `Pintura`
   - `Suspension`
   - `CochePieza`
2. Crear DTOs:
   - `PiezaResponse`: datos comunes de pieza y detalle tÃ©cnico segÃºn tipo.
   - `PiezaRequest`: `nombre`, `tipoPieza`, `descripcion`, `precio`, `disponible`, `esCatalogo`.
   - `AsignarPiezaRequest`: `cocheId`, `piezaId`, `cantidad`, `notas`.
   - `CochePiezaResponse`: `id`, `cocheId`, `piezaId`, `nombrePieza`, `tipoPieza`, `precio`, `cantidad`, `subtotal`, `notas`.
3. AÃ±adir mÃ©todos en repositorios:
   - `PiezaRepository.findByDisponibleTrue()`
   - `PiezaRepository.findByTipoPiezaIgnoreCase(String tipoPieza)`
   - `PiezaRepository.findByEsCatalogoTrue()`
   - `CochePiezaRepository.findByCocheId(UUID cocheId)`
4. Crear endpoints:
   - `GET /api/piezas/catalogo`
   - `GET /api/piezas/tipo/{tipoPieza}`
   - `GET /api/coches/{cocheId}/piezas`
   - `POST /api/coches/{cocheId}/piezas`
   - `PUT /api/coches/{cocheId}/piezas/{cochePiezaId}`
   - `DELETE /api/coches/{cocheId}/piezas/{cochePiezaId}`
5. En `POST /api/coches/{cocheId}/piezas`:
   - Validar que el coche existe.
   - Validar que la pieza existe.
   - Validar que la pieza estÃ¡ disponible.
   - Si ya existe la pieza en el coche, actualizar cantidad en vez de duplicar.
   - Recalcular `precioTotal`.
6. Crear datos iniciales de piezas:
   - 4 motores.
   - 4 pinturas.
   - 4 suspensiones.
   - 4 piezas aerodinÃ¡micas.
   - Cada pieza debe tener precio y descripciÃ³n Ãºtil para demo.
7. Probar:

```bash
curl http://localhost:8080/api/piezas/catalogo
curl http://localhost:8080/api/piezas/tipo/MOTOR
curl -X POST http://localhost:8080/api/coches/ID_COCHE/piezas -H "Content-Type: application/json" -d "{\"piezaId\":\"ID_PIEZA\",\"cantidad\":1,\"notas\":\"Montaje demo\"}"
curl http://localhost:8080/api/coches/ID_COCHE/piezas
```

## Frontend Ionic-Angular

1. Crear modelos:
   - `part.model.ts`
   - `car-part.model.ts`
2. Crear `PartService`:
   - `getCatalogParts()`.
   - `getPartsByType(type: string)`.
   - `getCarParts(carId: string)`.
   - `addPartToCar(carId: string, piezaId: string, cantidad: number, notas?: string)`.
   - `updateCarPart(carId: string, cochePiezaId: string, cantidad: number, notas?: string)`.
   - `removeCarPart(carId: string, cochePiezaId: string)`.
3. Completar pantalla `/personalizar/:id`:
   - Crear tabs o segmentos: motor, pintura, suspensiÃ³n, aerodinÃ¡mica.
   - Mostrar piezas por categorÃ­a.
   - Cada pieza debe mostrar nombre, descripciÃ³n, precio y atributos tÃ©cnicos.
   - BotÃ³n para seleccionar o quitar pieza.
   - Mostrar piezas seleccionadas.
   - Mostrar precio total actualizado.
4. AÃ±adir comportamiento:
   - Al seleccionar pieza, llamar backend.
   - Al quitar pieza, llamar backend.
   - Mostrar toast de Ã©xito/error.
   - Actualizar resumen sin recargar toda la app.
5. Ajustar diseÃ±o segÃºn Figma:
   - Cards o listas tal como estÃ© diseÃ±ado.
   - Colores de selecciÃ³n.
   - Precio siempre visible si el diseÃ±o lo contempla.
6. Prueba final:
   - Crear coche personalizado.
   - AÃ±adir motor, pintura, suspensiÃ³n y aerodinÃ¡mica.
   - Quitar una pieza.
   - Ver que el precio total cambia.
   - Ir a garaje y comprobar que el precio coincide.

## Memoria

Documentar:

- Tipos de piezas.
- CÃ³mo se asignan piezas a un coche.
- CÃ³mo se recalcula el precio.
- Capturas del configurador.
- Tiempo empleado.

---

# Persona 5 - Solicitudes de piezas personalizadas y panel admin

## Backend

1. Revisar `SolicitudPieza`, `SolicitudPiezaRepository`, `SolicitudPiezaService` y `SolicitudPiezaController`.
2. Crear DTOs:
   - `SolicitudPiezaRequest`: `usuarioId`, `cocheId`, `descripcion`.
   - `SolicitudPiezaResponse`: `id`, `usuarioId`, `usuarioNombre`, `cocheId`, `cocheNombre`, `descripcion`, `estado`, `respuestaAdmin`, `createdAt`, `revisadoAt`.
   - `RevisarSolicitudRequest`: `estado`, `respuestaAdmin`.
3. Definir estados:
   - `PENDIENTE`
   - `APROBADA`
   - `RECHAZADA`
4. AÃ±adir mÃ©todos en repositorio:
   - `findByUsuarioId(UUID usuarioId)`
   - `findByCocheId(UUID cocheId)`
   - `findByEstadoIgnoreCase(String estado)`
5. Crear endpoints:
   - `GET /api/solicitudes-pieza`
   - `GET /api/solicitudes-pieza/{id}`
   - `GET /api/solicitudes-pieza/usuario/{usuarioId}`
   - `GET /api/solicitudes-pieza/coche/{cocheId}`
   - `GET /api/solicitudes-pieza/estado/{estado}`
   - `POST /api/solicitudes-pieza`
   - `PATCH /api/solicitudes-pieza/{id}/revisar`
   - `DELETE /api/solicitudes-pieza/{id}`
6. En creaciÃ³n:
   - Validar usuario.
   - Validar coche.
   - Guardar estado inicial `PENDIENTE`.
   - Guardar `createdAt` automÃ¡tico.
7. En revisiÃ³n:
   - Validar que estado sea `APROBADA` o `RECHAZADA`.
   - Guardar `respuestaAdmin`.
   - Guardar `revisadoAt` con fecha actual.
8. Probar:

```bash
curl -X POST http://localhost:8080/api/solicitudes-pieza -H "Content-Type: application/json" -d "{\"usuarioId\":\"ID_USUARIO\",\"cocheId\":\"ID_COCHE\",\"descripcion\":\"Quiero un alerÃ³n especial inspirado en MÃ³naco\"}"
curl http://localhost:8080/api/solicitudes-pieza/estado/PENDIENTE
curl -X PATCH http://localhost:8080/api/solicitudes-pieza/ID_SOLICITUD/revisar -H "Content-Type: application/json" -d "{\"estado\":\"APROBADA\",\"respuestaAdmin\":\"Se puede fabricar para la demo\"}"
```

## Frontend Ionic-Angular

1. Crear modelos:
   - `custom-request.model.ts`
   - `review-request.model.ts`
2. Crear `CustomRequestService`:
   - `createRequest(usuarioId, cocheId, descripcion)`.
   - `getRequestsByUser(usuarioId)`.
   - `getAllRequests()`.
   - `getRequestsByStatus(estado)`.
   - `reviewRequest(id, estado, respuestaAdmin)`.
3. En pantalla `/personalizar/:id`, aÃ±adir bloque:
   - Formulario para pedir pieza personalizada.
   - Campo de texto largo para descripciÃ³n.
   - BotÃ³n enviar.
   - Validar que la descripciÃ³n no estÃ© vacÃ­a.
   - Mostrar toast de solicitud enviada.
4. Crear pantalla `Mis solicitudes`:
   - Ruta: `/solicitudes`.
   - Mostrar solicitudes del usuario logueado.
   - Mostrar estado con color.
   - Mostrar respuesta del admin si existe.
5. Crear pantalla admin:
   - Ruta: `/admin/solicitudes`.
   - Solo visible si `rol === 'ADMIN'`.
   - Listar solicitudes pendientes.
   - Ver descripciÃ³n completa.
   - Botones aprobar/rechazar.
   - Campo para respuesta admin.
6. Integrar navegaciÃ³n:
   - Usuario cliente ve `Mis solicitudes`.
   - Admin ve `Panel solicitudes`.
7. Prueba final:
   - Cliente crea solicitud desde personalizaciÃ³n.
   - Cliente la ve como pendiente.
   - Admin entra y la aprueba/rechaza.
   - Cliente refresca y ve la respuesta.

## Memoria

Documentar:

- Flujo de solicitud de pieza personalizada.
- Estados usados.
- Diferencia entre vista cliente y vista admin.
- Capturas de ambas pantallas.
- Tiempo empleado.

---

# Persona 6 - Ventas, checkout simulado e historial de compras

## Backend

1. Revisar `Venta`, `VentaRepository`, `VentaService` y `VentaController`.
2. Crear DTOs:
   - `VentaRequest`: `usuarioId`, `cocheId`, `metodoPago`.
   - `VentaResponse`: `id`, `usuarioId`, `usuarioNombre`, `cocheId`, `cocheNombre`, `montoTotal`, `estadoPago`, `metodoPago`, `fechaVenta`.
3. Definir estados:
   - `PENDIENTE`
   - `PAGADO`
   - `CANCELADO`
4. AÃ±adir mÃ©todos en repositorio:
   - `findByUsuarioId(UUID usuarioId)`
   - `findByCocheId(UUID cocheId)`
   - `existsByCocheId(UUID cocheId)`
5. Crear endpoints:
   - `GET /api/ventas`
   - `GET /api/ventas/{id}`
   - `GET /api/ventas/usuario/{usuarioId}`
   - `POST /api/ventas`
   - `PATCH /api/ventas/{id}/estado`
   - `DELETE /api/ventas/{id}`
6. En creaciÃ³n de venta:
   - Validar usuario.
   - Validar coche.
   - Validar que el coche no sea base.
   - Validar que ese coche no tenga ya una venta.
   - Tomar `montoTotal` desde `coche.precioTotal`.
   - Guardar `estadoPago = PAGADO` para demo, o `PENDIENTE` si se quiere simular confirmaciÃ³n.
   - Guardar `fechaVenta` con fecha actual.
7. Probar:

```bash
curl -X POST http://localhost:8080/api/ventas -H "Content-Type: application/json" -d "{\"usuarioId\":\"ID_USUARIO\",\"cocheId\":\"ID_COCHE\",\"metodoPago\":\"Tarjeta demo\"}"
curl http://localhost:8080/api/ventas/usuario/ID_USUARIO
```

## Frontend Ionic-Angular

1. Crear modelos:
   - `sale.model.ts`
   - `sale-request.model.ts`
2. Crear `SaleService`:
   - `createSale(usuarioId, cocheId, metodoPago)`.
   - `getSalesByUser(usuarioId)`.
   - `getSaleById(id)`.
3. Crear pantalla checkout:
   - Ruta: `/checkout/:cocheId`.
   - Mostrar resumen del coche.
   - Mostrar piezas seleccionadas.
   - Mostrar precio total.
   - Selector de mÃ©todo de pago simulado.
   - BotÃ³n confirmar compra.
4. En confirmaciÃ³n:
   - Llamar `POST /api/ventas`.
   - Mostrar pantalla o modal de compra completada.
   - Navegar a historial.
5. Crear pantalla historial:
   - Ruta: `/compras`.
   - Mostrar ventas del usuario.
   - Cada venta muestra coche, fecha, mÃ©todo, estado y total.
   - Permitir abrir detalle de compra si el Figma lo contempla.
6. Integrar con personalizaciÃ³n:
   - En `/personalizar/:id`, aÃ±adir botÃ³n `Comprar` o texto de Figma.
   - Navegar a checkout.
7. Prueba final:
   - Crear coche personalizado.
   - AÃ±adir piezas.
   - Ir a checkout.
   - Confirmar compra.
   - Ver historial.
   - Intentar comprar el mismo coche dos veces y comprobar error controlado.

## Memoria

Documentar:

- Checkout simulado.
- CÃ³mo se calcula el importe.
- Endpoints de ventas.
- Capturas de checkout e historial.
- Tiempo empleado.

---

# Persona 7 - ReseÃ±as, valoraciones y perfil del usuario

## Backend

1. Revisar `Resenia`, `ReseniaRepository`, `ReseniaService` y `ReseniaController`.
2. Crear DTOs:
   - `ReseniaRequest`: `usuarioId`, `cocheId`, `puntuacion`, `comentario`.
   - `ReseniaResponse`: `id`, `usuarioId`, `usuarioNombre`, `cocheId`, `cocheNombre`, `puntuacion`, `comentario`, `createdAt`.
   - `ResumenReseniasResponse`: `cocheId`, `media`, `totalResenias`.
3. AÃ±adir validaciones:
   - `puntuacion` entre 1 y 5.
   - `comentario` obligatorio o mÃ­nimo 3 caracteres.
4. AÃ±adir mÃ©todos en repositorio:
   - `findByCocheId(UUID cocheId)`
   - `findByUsuarioId(UUID usuarioId)`
   - `existsByUsuarioIdAndCocheId(UUID usuarioId, UUID cocheId)`
5. Crear endpoints:
   - `GET /api/resenias`
   - `GET /api/resenias/{id}`
   - `GET /api/resenias/coche/{cocheId}`
   - `GET /api/resenias/usuario/{usuarioId}`
   - `GET /api/resenias/coche/{cocheId}/resumen`
   - `POST /api/resenias`
   - `PUT /api/resenias/{id}`
   - `DELETE /api/resenias/{id}`
6. En creaciÃ³n:
   - Validar usuario.
   - Validar coche.
   - Evitar duplicado de reseÃ±a del mismo usuario para el mismo coche.
7. Crear o completar endpoints de perfil:
   - Reutilizar `GET /api/usuarios/{id}`.
   - `PUT /api/usuarios/{id}` para editar nombre/email/password.
8. Probar:

```bash
curl -X POST http://localhost:8080/api/resenias -H "Content-Type: application/json" -d "{\"usuarioId\":\"ID_USUARIO\",\"cocheId\":\"ID_COCHE\",\"puntuacion\":5,\"comentario\":\"Muy buen configurador\"}"
curl http://localhost:8080/api/resenias/coche/ID_COCHE
curl http://localhost:8080/api/resenias/coche/ID_COCHE/resumen
```

## Frontend Ionic-Angular

1. Crear modelos:
   - `review.model.ts`
   - `review-summary.model.ts`
2. Crear `ReviewService`:
   - `getReviewsByCar(cocheId)`.
   - `getReviewsByUser(usuarioId)`.
   - `getReviewSummary(cocheId)`.
   - `createReview(usuarioId, cocheId, puntuacion, comentario)`.
   - `updateReview(id, payload)`.
   - `deleteReview(id)`.
3. AÃ±adir reseÃ±as a pantalla de detalle de coche:
   - Mostrar media.
   - Mostrar total.
   - Listar comentarios.
   - Mostrar estrellas o componente equivalente segÃºn Figma.
4. Crear formulario de reseÃ±a:
   - Selector 1-5.
   - Textarea comentario.
   - BotÃ³n publicar.
   - Validaciones visuales.
5. Crear pantalla perfil:
   - Ruta: `/perfil`.
   - Mostrar nombre, email y rol.
   - Permitir editar nombre y email.
   - Mostrar reseÃ±as del usuario.
   - BotÃ³n cerrar sesiÃ³n si no lo hizo P1 en layout.
6. Prueba final:
   - Login cliente.
   - Abrir coche.
   - Crear reseÃ±a.
   - Ver media actualizada.
   - Ir a perfil y ver reseÃ±a propia.
   - Intentar crear reseÃ±a duplicada y mostrar error controlado.

## Memoria

Documentar:

- Sistema de puntuaciÃ³n.
- Validaciones.
- Endpoints de reseÃ±as.
- Pantalla de perfil.
- Tiempo empleado.

---

# Persona 8 - IntegraciÃ³n, calidad, datos demo, documentaciÃ³n y entrega

## Backend

1. Revisar que todas las personas han usado DTOs y no devuelven entidades con relaciones rotas.
2. Revisar que todos los controladores devuelven cÃ³digos correctos:
   - `200` en lectura.
   - `201` en creaciÃ³n si se implementa.
   - `204` en borrado.
   - `400` en validaciÃ³n.
   - `404` si no existe recurso.
3. Crear o unificar `DataInitializer`:
   - Usuario admin: `admin@gcs.com` / `1234`.
   - Usuario cliente: `cliente@gcs.com` / `1234`.
   - Coches base.
   - Piezas por categorÃ­a.
   - Alguna reseÃ±a inicial.
   - Alguna solicitud inicial.
4. Crear documentaciÃ³n de endpoints:
   - Archivo sugerido: `backend/API.md`.
   - Para cada endpoint poner mÃ©todo, ruta, body de ejemplo y respuesta esperada.
5. Crear colecciÃ³n simple de pruebas manuales:
   - Archivo sugerido: `backend/PRUEBAS.md`.
   - Incluir comandos `curl` ordenados: usuarios, coches, piezas, solicitudes, ventas, reseÃ±as.
6. Ejecutar pruebas:

```bash
cd GCS/backend
./mvnw test
```

En Windows:

```bash
cd GCS\backend
mvnw.cmd test
```

7. Comprobar arranque limpio:
   - Borrar contenedor si hace falta solo si el equipo lo acepta.
   - Levantar PostgreSQL.
   - Arrancar Spring Boot.
   - Ver que se cargan datos demo sin duplicarse.
8. Revisar que no se suben:
   - `node_modules/`
   - `.angular/`
   - `target/` si el equipo decide ignorarlo.
   - Archivos temporales.

## Frontend Ionic-Angular

1. Revisar integraciÃ³n completa:
   - Login.
   - CatÃ¡logo.
   - Detalle.
   - PersonalizaciÃ³n.
   - Garaje.
   - Solicitudes.
   - Admin.
   - Checkout.
   - Historial.
   - ReseÃ±as.
   - Perfil.
2. Unificar estilo:
   - Colores del Figma.
   - TipografÃ­a.
   - Espaciados.
   - Iconos.
   - Botones.
   - Estados de carga/error/vacÃ­o.
3. Revisar navegaciÃ³n:
   - No debe haber rutas muertas.
   - BotÃ³n atrÃ¡s correcto.
   - Tabs/menÃº coherente.
   - Admin no debe ver pantallas innecesarias de cliente si el diseÃ±o no lo contempla.
4. Revisar servicios Angular:
   - Todos usan `environment.apiUrl`.
   - No hay URLs hardcodeadas fuera de environment.
   - Todos manejan errores.
5. Crear archivo `frontend/README.md`:
   - CÃ³mo instalar.
   - CÃ³mo ejecutar.
   - Usuario admin demo.
   - Usuario cliente demo.
   - Rutas principales.
6. Probar en vista mÃ³vil:
   - Chrome devtools o dispositivo.
   - Comprobar que textos no se salen.
   - Comprobar que botones son accesibles.
   - Comprobar que imÃ¡genes cargan.
7. Preparar demo:
   - Guion de 5-7 minutos.
   - Orden recomendado:
     1. Login cliente.
     2. CatÃ¡logo.
     3. Detalle de coche.
     4. PersonalizaciÃ³n con piezas.
     5. Solicitud personalizada.
     6. Checkout.
     7. ReseÃ±a.
     8. Login admin y revisiÃ³n de solicitud.
8. Crear `autores.txt` en la raÃ­z de `GCS/`:

```txt
Grupo: GCS
Integrantes:
- P1
- P2
- P3
- P4
- P5
- P6
- P7
- P8
```

Cuando se conozcan los nombres reales, sustituir P1-P8 por los nombres.

## Memoria

P8 coordina el documento final, pero cada persona debe entregar su parte. La memoria debe incluir:

- QuÃ© prototipo de Figma se ha implementado.
- Funcionalidades implementadas.
- Backend implementado.
- Frontend implementado.
- MetodologÃ­a usada: tradicional, vibe, SDD o mezcla.
- TemporalizaciÃ³n por persona.
- Problemas encontrados.
- Soluciones aplicadas.
- Conclusiones finales.
- Capturas de pantalla.
- Instrucciones para ejecutar.

## ComprobaciÃ³n final de entrega

Antes de entregar, P8 debe revisar:

- Existe `GCS/backend/`.
- Existe `GCS/frontend/`.
- Existe `GCS/reparto.md`.
- Existe `GCS/autores.txt`.
- Existe memoria/documento tÃ©cnico.
- El backend arranca.
- El frontend arranca.
- La demo se puede hacer sin tocar base de datos manualmente.
- No se incluyen `node_modules`, `.angular`, carpetas de cachÃ© ni paquetes pesados innecesarios.

---

# Resumen de equilibrio

- P1: infraestructura comÃºn, usuarios, login y base visual.
- P2: catÃ¡logo de coches base.
- P3: detalle, duplicado y garaje.
- P4: piezas y configurador.
- P5: solicitudes y administraciÃ³n.
- P6: ventas y checkout.
- P7: reseÃ±as y perfil.
- P8: integraciÃ³n, datos demo, documentaciÃ³n, pruebas y entrega.

Cada persona tiene backend, frontend, pruebas y memoria. Las tareas son verticales para que cada una pueda demostrar una funcionalidad completa de principio a fin.

---

<!-- FIGMA_SCREENS_START -->
## Pantallas encontradas en Figma

Archivo Figma importado: `Tienda de F1 - Grupo V` (`c9Ts62YhbLurybZ1jotdIe`).

### Car Builder - Pablo
- Panel de Control (Flota) (`101:2`) - 1280x1024
- Solicitud de Piezas (Prioridad baja) (`137:823`) - 1280x1024
- Solicitud de Piezas (Prioridad media) (`137:609`) - 1280x1024
- Solicitud de Piezas (Prioridad crÃ­tica) (`101:274`) - 1280x1024
- Constructor TÃ©cnico (Motor) (`101:494`) - 1280x1024
- CatÃ¡logo de Piezas F1 (`101:685`) - 1280x1610.5
- CatÃ¡logo de VehÃ­culos F1 (`101:1026`) - 1280x1173
- Footer / Technical Status Bar (`101:1168`) - 1024x55
- Panel de Administrador (Rechazar solicitud) (`137:368`) - 1280x1024
- Panel de Administrador (Aceptar solicitud) (`137:47`) - 1280x1024
- Panel de Administrador (`101:1252`) - 1280x1024

### Superpagina de formula1 del mejor grupo
- Constructor de VehÃ­culos (`167:2`) - 1608x1024
- CatÃ¡logo de Piezas (`167:260`) - 2256x1272
- Panel de Control (`167:592`) - 1280x1723.5
- Solicitud de Piezas (`167:1129`) - 1650x1050
- AdministraciÃ³n de Sistemas (`167:1361`) - 1451x1257.5
- Body (`212:848`) - 1280x1024
- Constructor - PestaÃ±a Motor (Exacto) (`216:1125`) - 1280x1024
- Constructor - PestaÃ±a Pintura (Exacto) (`216:1390`) - 1280x1024

### Superpagina de formula1 del mejor grupo 2
- My Garage - Kinetic Lab (`111:195`) - 1767x1334
- Car Selector - Kinetic Lab (`111:372`) - 1786x1259
- Configurator Hub (`111:600`) - 1788x1036
- Engineering Deep Dive - Kinetic Lab v2 (`111:792`) - 1950x1243
- Admin Dashboard (`111:1112`) - 1921x1246
- Component Library (`111:1448`) - 1741x1144
- Checkout - Kinetic Lab (`111:1693`) - 1737x1185

### Inicio
- Desktop - 1 (`30:31`) - 1440x1024
- Card Grid Reviews (`34:187`) - 1200x422
- Search (`36:369`) - 223x40

Ver detalle completo en Figma/frames.md, resumen en Figma/summary.md y estilos en Figma/figma-styles.css.
<!-- FIGMA_SCREENS_END -->

