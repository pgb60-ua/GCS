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
  usuarioId?: string;
  createdAt?: string;
}
