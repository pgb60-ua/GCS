# GCS

GCS Javi Patas Compra un Dominio por un céntimo esta vez si punto com

# Esquema

```mermaid
erDiagram
  USUARIO {
    uuid id PK
    string nombre
    string email
    string password
    string rol
    timestamp created\_at
  }

  COCHE {
    uuid id PK
    string nomenclatura
    string equipo\_f1
    string temporada
    string descripcion
    decimal precio\_base
    string imagen\_url
    boolean es\_base
    decimal precio\_total
    uuid usuario\_id FK
    timestamp created\_at
  }

  PIEZA {
    uuid id PK
    string nombre
    string tipo\_pieza
    string descripcion
    decimal precio
    boolean disponible
    boolean es\_catalogo
  }

  AERODINAMICA {
    uuid id PK
    uuid pieza\_id FK
    decimal coeficiente\_arrastre
    string material
    string zona
  }

  MOTOR {
    uuid id PK
    uuid pieza\_id FK
    int potencia\_cv
    string combustible
    decimal cilindrada

&#x20;   string fabricante

&#x20;   string tipo
  }

  PINTURA {
    uuid id PK
    uuid pieza\_id FK
    string color\_hex
    string acabado
    boolean livery\_oficial
  }

  SUSPENSION {
    uuid id PK
    uuid pieza\_id FK
    string tipo
    string material
    decimal dureza

&#x20;   string eje
  }

  COCHE\_PIEZA {
    uuid id PK
    uuid coche\_id FK
    uuid pieza\_id FK
    int cantidad
    string notas
  }

  SOLICITUD\_PIEZA {
    uuid id PK
    uuid usuario\_id FK
    uuid coche\_id FK
    string descripcion
    string estado
    string respuesta\_admin
    timestamp created\_at
    timestamp revisado\_at
  }

  VENTA {
    uuid id PK
    uuid usuario\_id FK
    uuid coche\_id FK
    decimal monto\_total
    string estado\_pago
    string metodo\_pago
    timestamp fecha\_venta
  }

  RESENIA {
    uuid id PK
    uuid usuario\_id FK
    uuid coche\_id FK
    int puntuacion
    string comentario
    timestamp created\_at
  }

  USUARIO ||--o{ COCHE : "crea / personaliza"
  USUARIO ||--o{ SOLICITUD\_PIEZA : "solicita"
  USUARIO ||--o{ VENTA : "realiza"
  USUARIO ||--o{ RESENIA : "escribe"

  COCHE ||--o{ COCHE\_PIEZA : "contiene"
  COCHE ||--|| VENTA : "se vende en"
  COCHE ||--o{ SOLICITUD\_PIEZA : "incluye"
  COCHE ||--o{ RESENIA : "recibe"

  PIEZA ||--o{ COCHE\_PIEZA : "usada en"
  PIEZA ||--o| AERODINAMICA : "tiene"
  PIEZA ||--o| MOTOR : "tiene"
  PIEZA ||--o| PINTURA : "tiene"
  PIEZA ||--o| SUSPENSION : "tiene"
```

