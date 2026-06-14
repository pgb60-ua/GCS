export interface CustomRequest {
  id: string;
  usuarioId: string;
  usuarioNombre: string;
  cocheId: string;
  cocheNombre: string;
  descripcion: string;
  estado: 'PENDIENTE' | 'APROBADA' | 'RECHAZADA';
  respuestaAdmin?: string;
  createdAt: string;
  revisadoAt?: string;
}
