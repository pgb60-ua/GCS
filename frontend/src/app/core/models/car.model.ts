export interface Car {
  id: string;
  nomenclatura: string;
  equipoF1: string;
  temporada: string;
  descripcion: string | null;
  precioBase: number;
  precioTotal: number;
  imagenUrl: string | null;
  esBase: boolean;
  usuarioId?: string | null;
  createdAt?: string;
}

export interface CarSummary {
  id: string;
  nomenclatura: string;
  equipoF1: string;
  temporada: string;
  descripcion: string | null;
  precioBase: number;
  precioTotal: number;
  imagenUrl: string | null;
  esBase: boolean;
  usuarioId?: string | null;
  piezas: CarPartSummary[];
}

export interface CarPartSummary {
  id: string;
  nombre: string;
  tipoPieza: string;
  precio: number;
  cantidad: number;
  subtotal: number;
  notas: string | null;
}
