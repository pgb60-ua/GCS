# Pruebas manuales - Backend GCS

## Requisitos
- Backend corriendo en `http://localhost:8080`
- PostgreSQL levantado (ver `docker-compose.yml`)
- Usuarios de prueba cargados por `DataInitializer`

## Usuarios demo
- Admin: `admin@gcs.com` / `1234`
- Cliente: `cliente@gcs.com` / `1234`

## Endpoints de usuarios y auth

```bash
# Listar usuarios
curl http://localhost:8080/api/usuarios

# Login admin
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"email":"admin@gcs.com","password":"1234"}'

# Login cliente
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"email":"cliente@gcs.com","password":"1234"}'

# Crear usuario
curl -X POST http://localhost:8080/api/usuarios -H "Content-Type: application/json" -d '{"nombre":"Nuevo","email":"nuevo@gcs.com","password":"1234","rol":"CLIENTE"}'
```

## Endpoints de coches (Persona 3)

```bash
# Listar coches base
curl http://localhost:8080/api/coches/base

# Listar todos los coches
curl http://localhost:8080/api/coches

# Detalle de coche por ID
curl http://localhost:8080/api/coches/ID_COCHE

# Duplicar coche base para un usuario
curl -X POST http://localhost:8080/api/coches/ID_COCHE_BASE/duplicar -H "Content-Type: application/json" -d '{"usuarioId":"ID_USUARIO"}'

# Coches de un usuario
curl http://localhost:8080/api/coches/usuario/ID_USUARIO

# Resumen de coche (con piezas y precio calculado)
curl http://localhost:8080/api/coches/ID_COCHE/resumen

# Actualizar parcial coche personalizado (PATCH)
curl -X PATCH http://localhost:8080/api/coches/ID_COCHE -H "Content-Type: application/json" -d '{"nomenclatura":"Nuevo nombre","descripcion":"Nueva descripcion"}'

# Listar por equipo
curl http://localhost:8080/api/coches/equipo/Red%20Bull%20Racing
```

## Ejemplo de flujo completo Persona 3

```bash
# 1. Login cliente
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"email":"cliente@gcs.com","password":"1234"}')
USER_ID=$(echo $TOKEN | jq -r '.id')

# 2. Obtener un coche base (ejemplo: RB20)
BASE_ID=$(curl -s http://localhost:8080/api/coches/base | jq -r '.[0].id')

# 3. Duplicar coche base
curl -X POST http://localhost:8080/api/coches/$BASE_ID/duplicar -H "Content-Type: application/json" -d "{\"usuarioId\":\"$USER_ID\"}"

# 4. Ver garaje del usuario
curl http://localhost:8080/api/coches/usuario/$USER_ID

# 5. Ver resumen del coche duplicado (usar ID del nuevo coche)
NEW_ID=$(curl -s http://localhost:8080/api/coches/usuario/$USER_ID | jq -r '.[0].id')
curl http://localhost:8080/api/coches/$NEW_ID/resumen

# 6. Actualizar parcialmente
curl -X PATCH http://localhost:8080/api/coches/$NEW_ID -H "Content-Type: application/json" -d '{"nomenclatura":"RB20 Personalizado","descripcion":"Mi version"}'
```

## Notas
- El coche base tiene `esBase=true` y no se puede modificar con PATCH.
- El coche duplicado tiene `esBase=false` y `precioTotal = precioBase` inicialmente.
- El resumen calcula `precioTotal = precioBase + sum(precioPieza * cantidad)`.
