export interface Car {
  id: string;
  nomenclatura: string;
  equipoF1: string;
  temporada: string;
  descripcion: string;
  precioBase: number;
  precioTotal: number;
  imagenUrl: string;
  esBase: boolean;
  usuarioId?: string;
  createdAt?: string;
}
